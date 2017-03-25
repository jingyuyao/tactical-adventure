package com.jingyuyao.tactical.model.terrain;

import com.google.inject.Guice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainModuleTest {

  @Test
  public void can_create_terrains() {
    // Ma! I don't do shit!
    Guice.createInjector(new TerrainModule()).injectMembers(this);
  }
}