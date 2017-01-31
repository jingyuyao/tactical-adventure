package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainGraphsTest {

  private static final Coordinate CHARACTER_COORDINATE = new Coordinate(100, 100);
  private static final Coordinate BLOCKED_COORDINATE = new Coordinate(12, 12);
  private static final Coordinate DESTINATION = new Coordinate(50, 50);

  @Mock
  private Characters characters;
  @Mock
  private Terrains terrains;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain cannotPassTerrain;
  @Mock
  private Terrain blockedTerrain;
  @Mock
  private Character character;

  private TerrainGraphs terrainGraphs;

  @Before
  public void setUp() {
    terrainGraphs = new TerrainGraphs(characters, terrains);
  }

  @Test
  public void edge_cost_function() {
    when(characters.coordinates()).thenReturn(ImmutableList.of(BLOCKED_COORDINATE));
    when(terrain.getCoordinate()).thenReturn(DESTINATION);
    when(terrain.canHold(character)).thenReturn(true);
    when(terrain.getMovementPenalty()).thenReturn(123);
    when(blockedTerrain.getCoordinate()).thenReturn(BLOCKED_COORDINATE);
    when(cannotPassTerrain.getCoordinate()).thenReturn(DESTINATION);
    when(cannotPassTerrain.canHold(character)).thenReturn(false);

    Function<Terrain, Integer> function = terrainGraphs.createEdgeCostFunction(character);

    assertThat(function.apply(terrain)).isEqualTo(123);
    assertThat(function.apply(blockedTerrain)).isEqualTo(TerrainGraphs.BLOCKED);
    assertThat(function.apply(cannotPassTerrain)).isEqualTo(TerrainGraphs.BLOCKED);
  }
}