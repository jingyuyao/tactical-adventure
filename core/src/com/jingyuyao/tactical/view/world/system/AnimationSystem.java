package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.resource.LoopAnimation;
import com.jingyuyao.tactical.view.world.resource.SingleAnimation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnimationSystem extends EntitySystem {

  private final ComponentMapper<LoopAnimation> loopAnimationMapper;
  private final ComponentMapper<SingleAnimation> singleAnimationMapper;
  private final ComponentMapper<Frame> frameMapper;
  private ImmutableArray<Entity> loopAnimationEntities;
  private ImmutableArray<Entity> singleAnimationEntities;
  private float stateTime = 0f;

  @Inject
  AnimationSystem(
      ComponentMapper<LoopAnimation> loopAnimationMapper,
      ComponentMapper<SingleAnimation> singleAnimationMapper,
      ComponentMapper<Frame> frameMapper) {
    this.loopAnimationMapper = loopAnimationMapper;
    this.singleAnimationMapper = singleAnimationMapper;
    this.frameMapper = frameMapper;
    this.priority = SystemPriority.ANIMATION;
  }

  @Override
  public void addedToEngine(Engine engine) {
    loopAnimationEntities =
        engine.getEntitiesFor(Family.all(LoopAnimation.class, Frame.class).get());
    singleAnimationEntities =
        engine.getEntitiesFor(Family.all(SingleAnimation.class, Frame.class).get());
  }

  @Override
  public void update(float deltaTime) {
    stateTime += deltaTime;

    for (Entity entity : loopAnimationEntities) {
      LoopAnimation loopAnimation = loopAnimationMapper.get(entity);
      Frame frame = frameMapper.get(entity);
      frame.setTexture(loopAnimation.getKeyFrame(stateTime));
    }

    for (Entity entity : singleAnimationEntities) {
      SingleAnimation singleAnimation = singleAnimationMapper.get(entity);
      singleAnimation.advanceTime(deltaTime);
      if (singleAnimation.isDone()) {
        getEngine().removeEntity(entity);
      } else {
        Frame frame = frameMapper.get(entity);
        frame.setTexture(singleAnimation.getKeyFrame());
      }
    }
  }
}
