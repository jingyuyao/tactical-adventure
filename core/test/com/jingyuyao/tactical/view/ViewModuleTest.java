package com.jingyuyao.tactical.view;

import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import javax.inject.Inject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ViewModuleTest {

  @Bind
  @Mock
  private WorldController worldController;
  @Bind
  @Mock
  private CameraController cameraController;

  @Inject
  private WorldScreen worldScreen;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new ViewModule()).injectMembers(this);
  }
}