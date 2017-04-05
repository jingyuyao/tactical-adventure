package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.world2.component.Position;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class EffectsEntities {

  private final PooledEngine engine;
  private final Animations animations;

  @Inject
  EffectsEntities(
      PooledEngine engine,
      Animations animations) {
    this.engine = engine;
    this.animations = animations;
  }

  void addWeaponEffect(Coordinate coordinate, Weapon weapon, final MyFuture future) {
    SingleAnimation animation = animations.getWeapon(weapon.getName());
    future.completedBy(animation.getFuture());
    addEffect(coordinate, animation);
  }

  private void addEffect(Coordinate coordinate, SingleAnimation singleAnimation) {
    Entity entity = engine.createEntity();
    entity.add(createPosition(coordinate));
    entity.add(singleAnimation);
    engine.addEntity(entity);
  }

  private Position createPosition(Coordinate coordinate) {
    Position position = engine.createComponent(Position.class);
    position.setX(coordinate.getX());
    position.setY(coordinate.getY());
    position.setZ(WorldZIndex.EFFECTS);
    return position;
  }
}
