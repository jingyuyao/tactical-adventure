package com.jingyuyao.tactical.view.world;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldModuleTest {

  @Bind
  @Mock
  private CameraController cameraController;
  @Bind
  @Mock
  private WorldController worldController;

  @Inject
  private WorldView worldView;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this),
        new MockGameModule(),
        new WorldModule()).injectMembers(this);
  }
}