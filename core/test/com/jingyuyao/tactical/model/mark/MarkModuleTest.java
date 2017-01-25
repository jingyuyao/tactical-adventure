package com.jingyuyao.tactical.model.mark;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.MapObject;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkModuleTest {

  @Inject
  private MarkingFactory markingFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new MarkModule()).injectMembers(this);
  }

  @Test
  public void marking_factory() {
    markingFactory.create(ImmutableMultimap.<MapObject, Marker>of());
  }
}