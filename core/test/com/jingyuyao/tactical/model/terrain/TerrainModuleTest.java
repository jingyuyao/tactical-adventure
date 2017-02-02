package com.jingyuyao.tactical.model.terrain;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Coordinate;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainModuleTest {

  @Inject
  private TerrainFactory terrainFactory;

  @Test
  public void can_create_terrains() {
    Guice.createInjector(BoundFieldModule.of(this), new TerrainModule()).injectMembers(this);
    terrainFactory.createLand(new Coordinate(0, 0));
    terrainFactory.createWater(new Coordinate(0, 1));
    terrainFactory.createObstructed(new Coordinate(2, 2));
  }
}