package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BattleModuleTest {

  @Bind
  @Mock
  @ModelEventBus
  private EventBus eventBus;

  @Inject
  private Battle battle;

  @Test
  public void create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new BattleModule()).injectMembers(this);
  }
}