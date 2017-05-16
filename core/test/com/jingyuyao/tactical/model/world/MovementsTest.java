package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Dijkstra.GetEdgeCost;
import com.jingyuyao.tactical.model.world.Movements.ShipCost;
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
  private Ship ship;

  private Movements movements;

  @Before
  public void setUp() {
    movements = new Movements(world, dijkstra);
  }

  @Test
  public void edge_cost_function_has_ship() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(cell.getTerrain()).thenReturn(terrain);

    GetEdgeCost function = new ShipCost(ship);

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void edge_cost_function_cannot_hold() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(ship)).thenReturn(false);

    GetEdgeCost function = new ShipCost(ship);

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void edge_cost_function_penalty() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHold(ship)).thenReturn(true);
    when(terrain.getMovementPenalty()).thenReturn(10);

    GetEdgeCost function = new ShipCost(ship);

    assertThat(function.getEdgeCost(cell)).isEqualTo(10);
  }
}