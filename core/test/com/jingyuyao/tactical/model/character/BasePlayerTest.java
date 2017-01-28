package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasePlayerTest {

  private static final Coordinate PLAYER_COORDINATE = new Coordinate(0, 0);

  @Mock
  private Multiset<Marker> markers;
  @Mock
  private PlayerData data;
  @Mock
  private List<Item> items;
  @Mock
  private EventBus eventBus;
  @Mock
  private TerrainGraphs terrainGraphs;
  @Mock
  private Characters characters;
  @Mock
  private MapState mapState;
  private Player player;

  @Before
  public void setUp() {
    player =
        new BasePlayer(PLAYER_COORDINATE, markers, data, items, eventBus, terrainGraphs,
            characters);
  }

  @Test
  public void select() {
    player.select(mapState);

    verify(mapState).select(player);
  }

  @Test
  public void get_actionable() {
    when(data.isActionable()).thenReturn(true);

    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void set_actionable() {
    player.setActionable(false);

    verify(data).setActionable(false);
  }
}