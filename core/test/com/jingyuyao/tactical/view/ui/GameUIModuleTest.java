package com.jingyuyao.tactical.view.ui;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.data.MessageLoader;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameUIModuleTest {

  @Bind
  @Mock
  private MessageLoader messageLoader;

  @Inject
  private GameUI gameUI;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new MockGameModule(), new GameUIModule())
        .injectMembers(this);
  }
}