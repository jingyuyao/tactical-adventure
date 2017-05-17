package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.ControllingState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.jingyuyao.tactical.view.world.resource.Markers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class ShipSystem extends IteratingSystem {

  private final Markers markers;
  private final Animations animations;
  private final ComponentMapper<ShipComponent> shipMapper;
  private final ComponentMapper<Frame> frameMapper;

  @Inject
  ShipSystem(
      Markers markers,
      Animations animations,
      ComponentMapper<ShipComponent> shipMapper,
      ComponentMapper<Frame> frameMapper) {
    super(Family.all(ShipComponent.class, Frame.class).get(), SystemPriority.SHIP);
    this.markers = markers;
    this.animations = animations;
    this.shipMapper = shipMapper;
    this.frameMapper = frameMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    ShipComponent shipComponent = shipMapper.get(entity);
    Frame frame = frameMapper.get(entity);
    Ship ship = shipComponent.getShip();
    switch (ship.getAllegiance()) {
      case PLAYER:
        if (ship.isControllable()) {
          frame.setColor(Colors.BLUE_300);
        } else {
          frame.setColor(Colors.GREY_500);
        }
        break;
      case ENEMY:
        frame.setColor(Colors.RED_500);
        break;
    }
  }

  @Subscribe
  void spawnShip(SpawnShip spawnShip) {
    Cell cell = spawnShip.getObject();
    Preconditions.checkArgument(cell.ship().isPresent());
    Ship ship = cell.ship().get();

    Position position = getEngine().createComponent(Position.class);
    position.set(cell.getCoordinate(), WorldZIndex.SHIP);

    ShipComponent shipComponent = getEngine().createComponent(ShipComponent.class);
    shipComponent.setShip(ship);

    Frame frame = getEngine().createComponent(Frame.class);

    Entity entity = getEngine().createEntity();
    entity.add(position);
    entity.add(shipComponent);
    entity.add(frame);
    entity.add(animations.get(ship));

    getEngine().addEntity(entity);
  }

  @Subscribe
  void removeShip(RemoveShip removeShip) {
    Entity entity = get(removeShip.getObject());
    entity.add(getEngine().createComponent(Remove.class));
  }

  @Subscribe
  void instantMoveShip(InstantMoveShip instantMoveShip) {
    Entity entity = get(instantMoveShip.getShip());
    Coordinate destination = instantMoveShip.getDestination().getCoordinate();
    Position position = getEngine().createComponent(Position.class);
    position.set(destination, WorldZIndex.SHIP);
    entity.add(position);
  }

  @Subscribe
  void moveShip(MoveShip moveShip) {
    Entity entity = get(moveShip.getShip());
    Moving moving = getEngine().createComponent(Moving.class);
    moving.setPath(smoothPath(moveShip.getPath().getTrack()));
    moving.setPromise(moveShip.getPromise());
    entity.add(moving);
  }

  @Subscribe
  void playerState(ControllingState controllingState) {
    activate(controllingState.getShip());
  }

  @Subscribe
  void activatedEnemy(ActivatedEnemy activatedEnemy) {
    activate(activatedEnemy.getObject());
  }

  @Subscribe
  void exitState(ExitState exitState) {
    deactivate();
  }

  /**
   * O(n).
   *
   * Could be constant time if we keep a map but then we would be holding references to entities
   * outside of the engine which is a bad practice.
   */
  private Entity get(final Ship ship) {
    return Iterables.find(getEntities(), new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        ShipComponent component = shipMapper.get(input);
        return component != null && component.getShip().equals(ship);
      }
    });
  }

  private void activate(Ship ship) {
    deactivate();
    Entity entity = get(ship);
    Frame frame = frameMapper.get(entity);
    frame.addOverlay(markers.getActivated());
  }

  private void deactivate() {
    for (Entity entity : getEntities()) {
      Frame frame = frameMapper.get(entity);
      frame.removeOverlay(markers.getActivated());
    }
  }

  /**
   * Smooth out a track for animating the ship. Removes any intermediate coordinate
   * that are part of the same straight line. This will reduce the amount of animation hiccups.
   */
  private List<Coordinate> smoothPath(List<Cell> track) {
    Preconditions.checkArgument(!track.isEmpty());
    if (track.size() == 1) {
      return ImmutableList.of(track.get(0).getCoordinate());
    }
    if (track.size() == 2) {
      return ImmutableList.of(track.get(1).getCoordinate());
    }

    ImmutableList.Builder<Coordinate> builder = ImmutableList.builder();
    Coordinate origin = track.get(0).getCoordinate();
    Coordinate second = track.get(1).getCoordinate();
    Coordinate last = second;
    Direction lastDirection = Direction.fromTo(origin, second);

    // reduce part of a straight path to a single 'pivot' coordinate
    for (int i = 2; i < track.size() - 1; i++) {
      Coordinate current = track.get(i).getCoordinate();
      Direction currentDirection = Direction.fromTo(last, current);
      if (!currentDirection.equals(lastDirection)) {
        builder.add(last);
        lastDirection = currentDirection;
      }
      last = current;
    }

    // we always want the target coordinate to be the last on in the path
    Coordinate target = track.get(track.size() - 1).getCoordinate();
    if (!Direction.fromTo(last, target).equals(lastDirection)) {
      builder.add(last);
    }
    builder.add(target);

    return builder.build();
  }
}
