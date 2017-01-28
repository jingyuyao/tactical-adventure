package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.map.Marker;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainModuleTest {

  @Bind
  @Mock
  @InitialMarkers
  private Multiset<Marker> markers;

  @Inject
  private TerrainFactory terrainFactory;

  @Test
  public void can_create_terrains() {
    Guice.createInjector(BoundFieldModule.of(this), new TerrainModule()).injectMembers(this);
    terrainFactory.createLand(new MapObjectData(new Coordinate(0, 0)));
    terrainFactory.createWater(new MapObjectData(new Coordinate(0, 1)));
    terrainFactory.createObstructed(new MapObjectData(new Coordinate(2, 2)));
  }
}