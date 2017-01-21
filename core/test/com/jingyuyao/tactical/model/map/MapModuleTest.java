package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
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
  private EventBus eventBus;
  @Bind
  @Mock
  private MarkingFactory markingFactory;

  @Inject
  private Characters characters;
  @Inject
  private Characters characters2;
  @Inject
  private Terrains terrains;
  @Inject
  private Terrains terrains2;
  @Inject
  private TerrainFactory terrainFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new MapModule()).injectMembers(this);
  }

  @Test
  public void can_create_terrains() {
    terrainFactory.create(new Coordinate(0, 0), Type.NORMAL);
  }

  @Test
  public void singletons() {
    assertThat(characters).isSameAs(characters2);
    assertThat(terrains).isSameAs(terrains2);
  }
}