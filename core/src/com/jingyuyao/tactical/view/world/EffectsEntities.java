package com.jingyuyao.tactical.view.world;

import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class EffectsEntities {

  private final EntityFactory entityFactory;
  private final Animations animations;

  @Inject
  EffectsEntities(EntityFactory entityFactory, Animations animations) {
    this.entityFactory = entityFactory;
    this.animations = animations;
  }

  void addWeaponEffect(Coordinate coordinate, Weapon weapon, final MyFuture future) {
    SingleAnimation animation = animations.getWeapon(weapon.getName());
    future.completedBy(animation.getFuture());
    entityFactory.animated(coordinate, WorldZIndex.EFFECTS, animation);
  }
}
