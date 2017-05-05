package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.battle.Battle2;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class EffectsSystem extends EntitySystem {

  private final Animations animations;

  @Inject
  EffectsSystem(Animations animations) {
    super(SystemPriority.EFFECTS);
    this.animations = animations;
  }

  @Subscribe
  void battle2(final Battle2 battle2) {
    Target target = battle2.getTarget();

    Position position = getEngine().createComponent(Position.class);
    position.set(target.getOrigin().getCoordinate(), WorldZIndex.EFFECTS);

    Frame frame = getEngine().createComponent(Frame.class);
    Optional<Direction> direction = target.getDirection();
    if (direction.isPresent()) {
      frame.setDirection(direction.get());
    }

    SingleAnimation animation = animations.getWeapon(battle2.getWeapon().getName());
    animation.getFuture().addCallback(new Runnable() {
      @Override
      public void run() {
        battle2.execute();
      }
    });

    Entity entity = getEngine().createEntity();
    entity.add(position);
    entity.add(frame);
    entity.add(animation);

    getEngine().addEntity(entity);
  }
}
