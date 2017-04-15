package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.jingyuyao.tactical.view.world.resource.Markers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterSystem extends EntitySystem {

  private final ECF ecf;
  private final Markers markers;
  private final Animations animations;
  private final ComponentMapper<CharacterComponent> characterMapper;
  private final ComponentMapper<Frame> frameMapper;
  private ImmutableArray<Entity> entities;

  @Inject
  CharacterSystem(
      ECF ecf,
      Markers markers,
      Animations animations,
      ComponentMapper<CharacterComponent> characterMapper,
      ComponentMapper<Frame> frameMapper) {
    super(SystemPriority.CHARACTER);
    this.ecf = ecf;
    this.markers = markers;
    this.animations = animations;
    this.characterMapper = characterMapper;
    this.frameMapper = frameMapper;
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(Family.all(CharacterComponent.class).get());
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Entity entity = ecf.entity();
    entity.add(ecf.position(cell.getCoordinate(), WorldZIndex.CHARACTER));
    entity.add(animations.getCharacter(cell.getCharacter().getName()));
    entity.add(ecf.character(cell.getCharacter()));
    if (cell.hasPlayer()) {
      entity.add(ecf.frame(Colors.BLUE_300));
      entity.add(ecf.player(cell.getPlayer()));
    } else if (cell.hasEnemy()) {
      entity.add(ecf.frame(Colors.RED_500));
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    Entity entity = get(removeCharacter.getObject());
    entity.add(ecf.component(Remove.class));
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
    Entity entity = get(instantMoveCharacter.getCharacter());
    Coordinate destination = instantMoveCharacter.getDestination().getCoordinate();
    entity.add(ecf.position(destination, WorldZIndex.CHARACTER));
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
    Entity entity = get(moveCharacter.getCharacter());
    Moving moving = ecf.component(Moving.class);
    moving.setPath(smoothPath(moveCharacter.getPath().getTrack()));
    moving.setFuture(moveCharacter.getFuture());
    entity.add(moving);
  }

  @Subscribe
  void playerState(PlayerState playerState) {
    activate(playerState.getPlayer());
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
  private Entity get(final Character character) {
    return Iterables.find(entities, new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        CharacterComponent component = characterMapper.get(input);
        return component != null && component.getCharacter().equals(character);
      }
    });
  }

  private void activate(Character character) {
    deactivate();
    Entity entity = get(character);
    Frame frame = frameMapper.get(entity);
    frame.addOverlay(markers.getActivated());
  }

  private void deactivate() {
    for (Entity entity : entities) {
      Frame frame = frameMapper.get(entity);
      frame.removeOverlay(markers.getActivated());
    }
  }

  /**
   * Smooth out a track for animating the character. Removes any intermediate coordinate
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
