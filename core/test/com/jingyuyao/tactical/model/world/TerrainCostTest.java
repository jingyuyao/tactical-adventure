package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ship.Ship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainCostTest {

  @Mock
  private Cell cell;
  @Mock
  private Terrain terrain;
  @Mock
  private Ship ship;

  @Test
  public void has_ship() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(cell.getTerrain()).thenReturn(terrain);

    GetEdgeCost function = new TerrainCost();

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void cannot_hold() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHoldShip()).thenReturn(false);

    GetEdgeCost function = new TerrainCost();

    assertThat(function.getEdgeCost(cell)).isEqualTo(GetEdgeCost.NO_EDGE);
  }

  @Test
  public void no_ship_can_hold() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());
    when(cell.getTerrain()).thenReturn(terrain);
    when(terrain.canHoldShip()).thenReturn(true);
    when(terrain.getMoveCost()).thenReturn(10);

    GetEdgeCost function = new TerrainCost();

    assertThat(function.getEdgeCost(cell)).isEqualTo(10);
  }
}