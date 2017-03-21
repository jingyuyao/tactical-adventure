package com.jingyuyao.tactical.view.resource;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.view.actor.ActorConfig;
import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceModuleTest {

  @Bind
  @Mock
  private ActorConfig actorConfig;
  @Bind
  @Mock
  private WorldConfig worldConfig;

  @Inject
  private Markers markers;
  @Inject
  private Animations animations;
  @Inject
  private AnimationTime animationTime;
  @Inject
  private Skin provideSkin;
  @Inject
  private TextureAtlas provideTextureAtlas;
  @Inject
  private AnimationFactory animationFactory;
  @Inject
  private TextureFactory textureFactory;

  @Before
  public void setUp() {
    when(actorConfig.getActorWorldSize()).thenReturn(1f);
    when(worldConfig.getTileSize()).thenReturn(32);
  }

  @Test
  public void can_create_module() {
    Guice
        .createInjector(
            BoundFieldModule.of(this), new MockGameModule(), new ResourceModule())
        .injectMembers(this);
  }
}