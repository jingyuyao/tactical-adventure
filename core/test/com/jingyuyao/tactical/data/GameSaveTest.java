package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameSaveTest {

  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private World world;
  @Mock
  private Ship worldShip1;
  @Mock
  private Ship worldShip2;
  @Mock
  private Ship worldShip3;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;

  private GameSave gameSave;

  @Before
  public void setUp() {
    gameSave = new GameSave(3, Lists.newArrayList(ship1, ship2));
    assertThat(gameSave.getPlayerShips()).containsExactly(ship1, ship2);
  }

  @Test
  public void replace_player_ships_from() {
    when(worldShip1.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(worldShip2.inGroup(ShipGroup.PLAYER)).thenReturn(false);
    when(worldShip3.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(world.getActiveShips()).thenReturn(ImmutableMap.of(cell1, worldShip1, cell2, worldShip2));
    when(world.getInactiveShips()).thenReturn(Collections.singletonList(worldShip3));

    gameSave.replacePlayerShipsFrom(world);

    assertThat(gameSave.getPlayerShips()).containsExactly(worldShip1, worldShip3);
  }
}