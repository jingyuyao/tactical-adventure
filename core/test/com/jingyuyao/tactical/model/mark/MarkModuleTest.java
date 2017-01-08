package com.jingyuyao.tactical.model.mark;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.common.Waiter;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkModuleTest {

  @Bind
  @Mock
  private Waiter waiter;
  @Bind
  @Mock
  private Terrains terrains;
  @Mock
  private Targets targets;
  @Inject
  private MarkingFactory markingFactory;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new MarkModule()).injectMembers(this);
  }
}