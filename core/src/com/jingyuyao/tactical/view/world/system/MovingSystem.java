package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.WorldCamera;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.ShipComponent;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MovingSystem extends IteratingSystem {

  private final WorldConfig worldConfig;
  private final WorldCamera worldCamera;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Moving> movingMapper;
  private final ComponentMapper<ShipComponent> shipComponentMapper;
  private final ComponentMapper<Frame> frameMapper;

  @Inject
  MovingSystem(
      WorldConfig worldConfig,
      WorldCamera worldCamera,
      ComponentMapper<Position> positionMapper,
      ComponentMapper<Moving> movingMapper,
      ComponentMapper<ShipComponent> shipComponentMapper,
      ComponentMapper<Frame> frameMapper) {
    super(Family.all(Position.class, Moving.class).get(), SystemPriority.MOVING);
    this.worldConfig = worldConfig;
    this.worldCamera = worldCamera;
    this.shipComponentMapper = shipComponentMapper;
    this.positionMapper = positionMapper;
    this.movingMapper = movingMapper;
    this.frameMapper = frameMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionMapper.get(entity);
    Moving moving = movingMapper.get(entity);
    int currentIndex = moving.getCurrentIndex();
    List<Coordinate> path = moving.getPath();
    Coordinate currentTarget = path.get(currentIndex);

    moveTowards(position, currentTarget, deltaTime);

    // Move camera along if the entity contains a non-player ship.
    if (shipComponentMapper.has(entity)) {
      ShipComponent shipComponent = shipComponentMapper.get(entity);
      if (!shipComponent.getShip().inGroup(ShipGroup.PLAYER)) {
        worldCamera.setCameraPosition(position.getX(), position.getY());
      }
    }

    if (frameMapper.has(entity) && shipComponentMapper.has(entity)) {
      for (Direction newDirection : positionToTarget(position, currentTarget).asSet()) {
        Frame frame = frameMapper.get(entity);
        frame.setDirection(newDirection);
      }
    }

    if (samePosition(position, currentTarget)) {
      int nextIndex = currentIndex + 1;
      if (nextIndex < path.size()) {
        moving.setCurrentIndex(nextIndex);
      } else {
        moving.getPromise().complete();
        entity.remove(Moving.class);
      }
    }
  }

  private boolean samePosition(Position position, Coordinate coordinate) {
    return position.getX() == (float) coordinate.getX()
        && position.getY() == (float) coordinate.getY();
  }

  private void moveTowards(Position position, Coordinate target, float delta) {
    float px = position.getX();
    float py = position.getY();
    float tx = target.getX();
    float ty = target.getY();
    float distance = delta * worldConfig.getShipMoveUnitPerSec();

    if (px < tx) {
      position.setX(Math.min(px + distance, tx));
    } else if (px > tx) {
      position.setX(Math.max(px - distance, tx));
    }

    if (py < ty) {
      position.setY(Math.min(py + distance, ty));
    } else if (py > ty) {
      position.setY(Math.max(py - distance, ty));
    }
  }

  private Optional<Direction> positionToTarget(Position position, Coordinate target) {
    float px = position.getX();
    float py = position.getY();
    int tx = target.getX();
    int ty = target.getY();
    boolean samePoint = px == tx && py == ty;
    boolean onlyOneAxisMatch = (px == tx) ^ (py == ty);

    if (!samePoint && onlyOneAxisMatch) {
      if (px < tx) {
        return Optional.of(Direction.RIGHT);
      } else if (px > tx) {
        return Optional.of(Direction.LEFT);
      }

      if (py < ty) {
        return Optional.of(Direction.UP);
      } else if (py > ty) {
        return Optional.of(Direction.DOWN);
      }
    }

    return Optional.absent();
  }
}
