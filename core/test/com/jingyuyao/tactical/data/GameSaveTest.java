package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_2;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.ship.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.List;
import java.util.Map;
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
  private Ship ship3;
  @Mock
  private World world;
  @Mock
  private Ship worldShip1;
  @Mock
  private Ship worldShip2;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;

  private List<Ship> inactive;
  private List<Ship> activate;
  private GameSave gameSave;

  @Before
  public void setUp() {
    inactive = Lists.newArrayList(ship1, ship2);
    activate = Lists.newArrayList(ship3);
    gameSave = new GameSave(3, inactive, activate);
  }

  @Test
  public void activate_ships_more_spawns() {
    Map<Coordinate, Ship> activated = gameSave.activateShips(ImmutableList.of(C0_0, C0_1, C0_2));

    assertThat(activated).containsExactly(C0_0, ship1, C0_1, ship2);
    assertThat(inactive).isEmpty();
    assertThat(activate).containsExactly(ship1, ship2, ship3);
  }

  @Test
  public void activate_ships_less_spawns() {
    Map<Coordinate, Ship> activated = gameSave.activateShips(ImmutableList.of(C0_0));

    assertThat(activated).containsExactly(C0_0, ship1);
    assertThat(inactive).containsExactly(ship2);
    assertThat(activate).containsExactly(ship1, ship3);
  }

  @Test
  public void replace_ships_from_world() {
    when(worldShip1.getAllegiance()).thenReturn(Allegiance.PLAYER);
    when(worldShip2.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, worldShip1, cell2, worldShip2));

    gameSave.replaceActiveShipsFrom(world);

    assertThat(inactive).containsExactly(ship1, ship2);
    assertThat(activate).containsExactly(worldShip1);
  }

  @Test
  public void deactivate_ships() {
    gameSave.deactivateShips();

    assertThat(inactive).containsExactly(ship1, ship2, ship3);
    assertThat(activate).isEmpty();
  }
}