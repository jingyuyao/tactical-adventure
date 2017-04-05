package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Position;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovingSystem extends IteratingSystem {

  private static final float MOVING_RATE = 5f;

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Moving> movingMapper;

  @Inject
  public MovingSystem(
      ComponentMapper<Position> positionMapper,
      ComponentMapper<Moving> movingMapper) {
    super(Family.all(Position.class, Moving.class).get());
    this.positionMapper = positionMapper;
    this.movingMapper = movingMapper;
    this.priority = SystemPriority.MOVING;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionMapper.get(entity);
    Moving moving = movingMapper.get(entity);
    int currentIndex = moving.getCurrentIndex();
    List<Coordinate> path = moving.getPath();
    Coordinate currentTarget = path.get(currentIndex);

    if (samePosition(position, currentTarget)) {
      int nextIndex = currentIndex + 1;
      if (nextIndex < path.size()) {
        moving.setCurrentIndex(nextIndex);
      } else {
        moving.getFuture().done();
        entity.remove(Moving.class);
      }
    } else {
      moveTowards(position, currentTarget, deltaTime);
    }
  }

  private boolean samePosition(Position position, Coordinate coordinate) {
    return position.getX() == (float) coordinate.getX()
        && position.getY() == (float) coordinate.getY();
  }

  private void moveTowards(Position position, Coordinate target, float delta) {
    // TODO: this is not very smooth as there is a bit of spazzing when transition between coords
    float px = position.getX();
    float py = position.getY();
    float tx = target.getX();
    float ty = target.getY();
    float distance = MOVING_RATE * delta;

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
}
