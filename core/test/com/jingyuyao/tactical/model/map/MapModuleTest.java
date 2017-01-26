package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapModuleTest {

  @Bind
  @Mock
  @ModelEventBus
  private EventBus eventBus;
  @Bind
  @Mock
  private Algorithms algorithms;

  @Inject
  private TerrainFactory terrainFactory;
  @Inject
  private Characters characters;
  @Inject
  private Terrains terrains;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new MapModule()).injectMembers(this);
  }

  @Test
  public void can_create_terrains() {
    terrainFactory.create(new Coordinate(0, 0), Type.NORMAL);
  }
}