package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EffectsSystem extends EntitySystem {

  private final ECF ecf;
  private final Animations animations;

  @Inject
  EffectsSystem(ECF ecf, Animations animations) {
    this.ecf = ecf;
    this.animations = animations;
    this.priority = SystemPriority.EFFECTS;
  }

  @Subscribe
  void attack(final Attack attack) {
    // TODO: distinguish on animation on select tile or an animation for every target tile
    Cell select = Iterables.getFirst(attack.getObject().getSelectCells(), null);
    if (select != null) {
      SingleAnimation animation = animations.getWeapon(attack.getWeapon().getName());
      attack.getFuture().completedBy(animation.getFuture());
      Entity entity = ecf.entity();
      entity.add(ecf.position(select.getCoordinate(), WorldZIndex.EFFECTS));
      entity.add(ecf.frame());
      entity.add(animation);
    }
  }
}
