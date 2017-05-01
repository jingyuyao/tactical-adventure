package com.jingyuyao.tactical.view.ui;

import com.google.inject.Guice;
import com.jingyuyao.tactical.MockGameModule;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameUIModuleTest {

  @Inject
  private GameUI gameUI;

  @Test
  public void can_create_module() {
    Guice.createInjector(new MockGameModule(), new GameUIModule()).injectMembers(this);
  }
}