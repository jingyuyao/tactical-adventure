package com.jingyuyao.tactical.controller;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldCamera;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllerModuleTest {

  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private WorldState worldState;
  @Bind
  @Mock
  private WorldCamera worldCamera;

  @Inject
  private WorldController worldController;
  @Inject
  private CameraController cameraController;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new ControllerModule())
        .injectMembers(this);
  }
}