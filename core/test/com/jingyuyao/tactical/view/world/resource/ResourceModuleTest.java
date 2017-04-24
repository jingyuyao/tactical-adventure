package com.jingyuyao.tactical.view.world.resource;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceModuleTest {

  @Bind
  @Mock
  private WorldConfig worldConfig;

  @Inject
  private Markers markers;
  @Inject
  private Animations animations;
  @Inject
  private TextureFactory textureFactory;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new ResourceModule()).injectMembers(this);
  }
}