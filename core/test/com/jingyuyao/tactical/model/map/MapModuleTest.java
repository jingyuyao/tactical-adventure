package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.World;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapModuleTest {

  @Bind
  @Mock
  @ModelEventBus
  private EventBus eventBus;
  @Bind
  @Mock
  private World world;

  @Inject
  private Movements movements;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new MapModule()).injectMembers(this);
  }
}