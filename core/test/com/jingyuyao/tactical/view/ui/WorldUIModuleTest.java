package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.google.inject.Guice;
import com.jingyuyao.tactical.MockGameModule;
import javax.inject.Inject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldUIModuleTest {

  @Inject
  private WorldUI worldUI;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(new MockGameModule(), new WorldUIModule()).injectMembers(this);
  }
}