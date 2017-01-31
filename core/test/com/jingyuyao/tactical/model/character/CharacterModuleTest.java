package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Movements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterModuleTest {

  @Bind
  @Mock
  private Movements movements;
  @Bind
  @Mock
  private Characters characters;
  @Bind
  @Mock
  @InitialMarkers
  private Multiset<Marker> markers;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }
}