package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Dijkstra.GetEdgeCost;
import com.jingyuyao.tactical.model.world.Movements.CharacterCost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
// TODO: finish me
public class MovementsTest {

  @Mock
  private World world;
  @Mock
  private Dijkstra dijkstra;
  @Mock
  private Cell cell;
  @Mock
  private Terrain terrain;
  @Mock
  private Character character;

  private Movements movements;

  @Before
  public void setUp() {
    movements = new Movements(world, dijkstra);
  }

  @Test
  public void edge_cost_function_has_character() {
    when(cell.character()).thenReturn(Optional.of(character));
    when(cell.getTerrain()).thenReturn(terrain);

    GetEdgeCost function = new CharacterCost(character);

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void edge_cost_function_cannot_hold() {
    when(cell.character()).thenReturn(Optional.<Character>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(character)).thenReturn(false);

    GetEdgeCost function = new CharacterCost(character);

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void edge_cost_function_penalty() {
    when(cell.character()).thenReturn(Optional.<Character>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(character)).thenReturn(true);
    when(terrain.getMovementPenalty()).thenReturn(10);

    GetEdgeCost function = new CharacterCost(character);

    assertThat(function.getEdgeCost(cell)).isEqualTo(10);
  }
}