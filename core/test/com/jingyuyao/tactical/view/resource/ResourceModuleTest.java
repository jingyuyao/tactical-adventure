package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceModuleTest {

  @Bind
  @Mock
  private TextureAtlas textureAtlas;

  @Inject
  private MarkerSprites markerSprites;
  @Inject
  private Animations animations;
  @Inject
  private AnimationTime animationTime;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new ResourceModule()).injectMembers(this);
  }
}