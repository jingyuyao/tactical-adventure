package com.jingyuyao.tactical.model.character;

import static org.mockito.Mockito.verify;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
// TODO: test retaliation
public class PassiveEnemyTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private Multiset<Marker> markers;
  @Mock
  private CharacterData data;
  @Mock
  private EventBus eventBus;
  @Mock
  private List<Item> items;
  @Mock
  private TerrainGraphs terrainGraphs;
  @Mock
  private Characters characters;
  @Mock
  private MovementFactory movementFactory;
  @Mock
  private MapState mapState;

  private PassiveEnemy enemy;

  @Before
  public void setUp() {
    enemy =
        new PassiveEnemy(
            COORDINATE, markers, data, items, eventBus, terrainGraphs, characters, movementFactory);
  }

  @Test
  public void select() {
    enemy.select(mapState);

    verify(mapState).select(enemy);
  }
}