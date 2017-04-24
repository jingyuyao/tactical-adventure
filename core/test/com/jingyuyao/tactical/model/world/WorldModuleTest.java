package com.jingyuyao.tactical.model.world;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelBus;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldModuleTest {

  @Bind
  @Mock
  private ModelBus modelBus;

  @Inject
  private World world;
  @Inject
  private CellFactory cellFactory;
  @Inject
  private Movements movements;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new WorldModule()).injectMembers(this);
  }
}