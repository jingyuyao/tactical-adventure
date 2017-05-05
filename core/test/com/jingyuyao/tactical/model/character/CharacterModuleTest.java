package com.jingyuyao.tactical.model.character;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterModuleTest {

  @Test
  public void can_create_module() {
    // Ma! I don't do shit!
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }
}