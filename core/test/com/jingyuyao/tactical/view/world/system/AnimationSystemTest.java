package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnimationSystemTest {

  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;
  @Mock
  private WorldTexture texture3;

  // Its better to depend on real Engine behavior rather than mocking
  private Engine engine;
  private AnimationSystem animationSystem;

  @Before
  public void setUp() {
    engine = new Engine();
    animationSystem =
        new AnimationSystem(
            ComponentMapper.getFor(LoopAnimation.class),
            ComponentMapper.getFor(SingleAnimation.class),
            ComponentMapper.getFor(Frame.class));
    assertThat(animationSystem.priority).isEqualTo(SystemPriority.ANIMATION);
  }

  @Test
  public void update() {
    // Can't mock these since Entity and Engine use bit masking based on actual class information
    LoopAnimation loopAnimation = new LoopAnimation(1, new WorldTexture[]{texture1, texture2});
    SingleAnimation singleAnimation = new SingleAnimation(1, new WorldTexture[]{texture3});
    Frame frame1 = new Frame();
    Frame frame2 = new Frame();

    engine.addSystem(animationSystem);
    Entity entity1 = engine.createEntity();
    entity1.add(loopAnimation);
    entity1.add(frame1);
    engine.addEntity(entity1);
    Entity entity2 = engine.createEntity();
    entity2.add(singleAnimation);
    entity2.add(frame2);
    engine.addEntity(entity2);

    engine.update(0.5f);

    assertThat(frame1.getTexture()).hasValue(texture1);
    assertThat(frame2.getTexture()).hasValue(texture3);

    engine.update(1f);

    assertThat(frame1.getTexture()).hasValue(texture2);
    assertThat(frame2.getTexture()).hasValue(texture3);
    assertThat(entity2.getComponent(Remove.class)).isNotNull();
  }
}