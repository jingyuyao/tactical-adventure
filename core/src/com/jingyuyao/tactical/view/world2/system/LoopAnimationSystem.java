package com.jingyuyao.tactical.view.world2.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoopAnimationSystem extends IteratingSystem {

  private final ComponentMapper<LoopAnimation> loopAnimationMapper;

  @Inject
  LoopAnimationSystem(ComponentMapper<LoopAnimation> loopAnimationMapper) {
    super(Family.all(LoopAnimation.class).get());
    // TODO: unify this into one animation system, use one() instead of all
    this.loopAnimationMapper = loopAnimationMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    LoopAnimation loopAnimation = loopAnimationMapper.get(entity);
    entity.add(loopAnimation.getCurrentFrame());
  }
}
