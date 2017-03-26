package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovementsTest {

  @Mock
  private World world;
  @Mock
  private Cell cell;
  @Mock
  private Terrain terrain;
  @Mock
  private Character character;

  private Movements movements;

  @Before
  public void setUp() {
    movements = new Movements(world);
  }

  @Test
  public void edge_cost_function_has_character() {
    when(cell.hasCharacter()).thenReturn(true);
    when(cell.getTerrain()).thenReturn(terrain);

    Function<Cell, Integer> function = movements.createEdgeCostFunction(character);

    assertThat(function.apply(cell)).isEqualTo(Movements.BLOCKED);
  }

  @Test
  public void edge_cost_function_cannot_hold() {
    when(cell.hasCharacter()).thenReturn(false);
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(character)).thenReturn(false);

    Function<Cell, Integer> function = movements.createEdgeCostFunction(character);

    assertThat(function.apply(cell)).isEqualTo(Movements.BLOCKED);
  }

  @Test
  public void edge_cost_function_penalty() {
    when(cell.hasCharacter()).thenReturn(false);
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(character)).thenReturn(true);
    when(terrain.getMovementPenalty()).thenReturn(10);

    Function<Cell, Integer> function = movements.createEdgeCostFunction(character);

    assertThat(function.apply(cell)).isEqualTo(10);
  }
}