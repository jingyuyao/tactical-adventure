package com.jingyuyao.tactical.model.battle;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BattleModuleTest {

  @Bind
  @Mock
  private Characters characters;
  @Bind
  @Mock
  private Terrains terrains;

  @Inject
  private PiercingFactory piercingFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new BattleModule()).injectMembers(this);
  }

  @Test
  public void can_create_factories() {
  }
}