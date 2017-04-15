package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldModuleTest {

  @Bind
  @Mock
  private WorldCamera worldCamera;
  @Bind
  @Mock
  private WorldController worldController;

  @Inject
  private WorldView worldView;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this),
        new MockGameModule(),
        new WorldModule()).injectMembers(this);
  }
}