package com.jingyuyao.tactical.model;

import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

  @Mock
  private Characters characters;
  @Mock
  private Terrains terrains;
  @Mock
  private MapState mapState;
  @Mock
  private Object object;
  @Mock
  private Iterable<Terrain> terrainIterable;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private State state;

  private Iterable<Player> playerIterable;
  private Iterable<Enemy> enemyIterable;
  private Model model;

  @Before
  public void setUp() {
    playerIterable = ImmutableList.of(player);
    enemyIterable = ImmutableList.of(enemy);
    model = new Model(characters, terrains, mapState);
  }

  @Test
  public void new_map() {
    model.loadMap(10, 5, terrainIterable, playerIterable, enemyIterable, state);

    verify(terrains).initialize(terrainIterable, 10, 5);
    verify(characters).add(player);
    verify(characters).add(enemy);
    verify(mapState).initialize(state);
  }
}