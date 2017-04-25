package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class EffectsSystem extends EntitySystem {

  private final ECF ecf;
  private final Animations animations;

  @Inject
  EffectsSystem(ECF ecf, Animations animations) {
    super(SystemPriority.EFFECTS);
    this.ecf = ecf;
    this.animations = animations;
  }

  @Subscribe
  void attack(final Attack attack) {
    Target target = attack.getObject();

    SingleAnimation animation = animations.getWeapon(attack.getWeapon().getName());
    attack.getFuture().completedBy(animation.getFuture());

    Frame frame = ecf.frame();
    Optional<Direction> direction = target.getDirection();
    if (direction.isPresent()) {
      frame.setDirection(direction.get());
    }

    Entity entity = ecf.entity();
    entity.add(ecf.position(target.getOrigin().getCoordinate(), WorldZIndex.EFFECTS));
    entity.add(frame);
    entity.add(animation);
  }
}
