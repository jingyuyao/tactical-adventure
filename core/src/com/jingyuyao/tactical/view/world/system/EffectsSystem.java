package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.EntitySystem;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EffectsSystem extends EntitySystem {

  private final EntityFactory entityFactory;
  private final Animations animations;

  @Inject
  EffectsSystem(EntityFactory entityFactory, Animations animations) {
    this.entityFactory = entityFactory;
    this.animations = animations;
    this.priority = SystemPriority.EFFECTS;
  }

  public void addWeaponEffect(Coordinate coordinate, Weapon weapon, final MyFuture future) {
    SingleAnimation animation = animations.getWeapon(weapon.getName());
    future.completedBy(animation.getFuture());
    entityFactory.animated(coordinate, WorldZIndex.EFFECTS, animation);
  }
}
