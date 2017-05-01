package com.jingyuyao.tactical.menu;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.data.DataManager;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuModuleTest {

  @Bind
  @Mock
  private GameState gameState;
  @Bind
  @Mock
  private DataManager dataManager;

  @Inject
  private StartMenu startMenu;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new MenuModule()).injectMembers(this);
  }
}