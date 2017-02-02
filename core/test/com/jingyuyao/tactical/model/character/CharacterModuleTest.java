package com.jingyuyao.tactical.model.character;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.battle.Battle;
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
  private Battle battle;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }
}