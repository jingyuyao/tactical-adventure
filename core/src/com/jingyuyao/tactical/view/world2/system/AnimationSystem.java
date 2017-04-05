package com.jingyuyao.tactical.view.world2.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jingyuyao.tactical.view.resource.AnimationTime;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnimationSystem extends EntitySystem {

  private final AnimationTime animationTime;
  private final ComponentMapper<LoopAnimation> loopAnimationMapper;
  private final ComponentMapper<SingleAnimation> singleAnimationMapper;
  private ImmutableArray<Entity> loopAnimationEntities;
  private ImmutableArray<Entity> singleAnimationEntities;

  @Inject
  AnimationSystem(
      AnimationTime animationTime,
      ComponentMapper<LoopAnimation> loopAnimationMapper,
      ComponentMapper<SingleAnimation> singleAnimationMapper) {
    this.animationTime = animationTime;
    this.loopAnimationMapper = loopAnimationMapper;
    this.singleAnimationMapper = singleAnimationMapper;
  }

  @Override
  public void addedToEngine(Engine engine) {
    loopAnimationEntities = engine.getEntitiesFor(Family.all(LoopAnimation.class).get());
    singleAnimationEntities = engine.getEntitiesFor(Family.all(SingleAnimation.class).get());
  }

  @Override
  public void update(float deltaTime) {
    animationTime.advanceStateTime(deltaTime);

    for (Entity entity : loopAnimationEntities) {
      LoopAnimation loopAnimation = loopAnimationMapper.get(entity);
      entity.add(loopAnimation.getCurrentFrame());
    }

    for (Entity entity : singleAnimationEntities) {
      SingleAnimation singleAnimation = singleAnimationMapper.get(entity);
      if (singleAnimation.isDone()) {
        entity.remove(SingleAnimation.class);
        entity.remove(WorldTexture.class);
      } else {
        entity.add(singleAnimation.getCurrentFrame());
      }
    }
  }
}
