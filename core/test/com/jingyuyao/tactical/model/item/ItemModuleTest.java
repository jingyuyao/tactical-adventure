package com.jingyuyao.tactical.model.item;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemModuleTest {

  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private Movements movements;

  @Test
  public void can_create_module() {
    // Ma! I don't do shit!
    Guice.createInjector(BoundFieldModule.of(this), new ItemModule()).injectMembers(this);
  }
}