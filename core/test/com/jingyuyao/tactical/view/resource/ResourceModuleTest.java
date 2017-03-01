package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
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
  private AssetManager assetManager;
  @Bind
  @Mock
  private ActorConfig actorConfig;
  @Bind
  @Mock
  private WorldConfig worldConfig;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private Skin skin;

  @Inject
  private MarkerSprites markerSprites;
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
    when(assetManager.get(ResourceModule.TEXTURE_ATLAS, TextureAtlas.class))
        .thenReturn(textureAtlas);
    when(assetManager.get(ResourceModule.SKIN, Skin.class)).thenReturn(skin);
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new ResourceModule()).injectMembers(this);
    assertThat(provideTextureAtlas).isSameAs(textureAtlas);
    assertThat(provideSkin).isSameAs(skin);
  }
}