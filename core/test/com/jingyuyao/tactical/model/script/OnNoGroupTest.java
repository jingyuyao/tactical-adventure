package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnNoGroupTest {

  @Mock
  private Turn turn;
  @Mock
  private World world;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;

  private OnNoGroup onNoGroup;

  @Before
  public void setUp() {
    onNoGroup = new OnNoGroup(ShipGroup.PLAYER);
  }

  @Test
  public void satisfied() {
    when(world.getActiveShips()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));

    assertThat(onNoGroup.onTurn(turn, world)).isTrue();
    assertThat(onNoGroup.onShipDestroyed(ship2, world)).isTrue();
  }

  @Test
  public void not_satisfied() {
    when(world.getActiveShips()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);

    assertThat(onNoGroup.onTurn(turn, world)).isFalse();
    assertThat(onNoGroup.onShipDestroyed(ship2, world)).isFalse();
  }
}