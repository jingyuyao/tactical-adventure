package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldSubscriberTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);

  @Mock
  private WorldView worldView;
  @Mock
  private WorldLoad worldLoad;
  @Mock
  private WorldReset worldReset;
  @Mock
  private Cell cell;
  @Mock
  private Terrain terrain;

  private WorldSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new WorldSubscriber(worldView);
  }

  @Test
  public void world_load() {
    when(worldLoad.getObject()).thenReturn(ImmutableList.of(cell));
    when(cell.getTerrain()).thenReturn(terrain);
    when(cell.getCoordinate()).thenReturn(COORDINATE);

    subscriber.worldLoad(worldLoad);

    verify(worldView).add(cell);
    verify(worldView).add(COORDINATE, terrain);
  }

  @Test
  public void world_reset() {
    subscriber.worldReset(worldReset);

    verify(worldView).reset();
  }
}