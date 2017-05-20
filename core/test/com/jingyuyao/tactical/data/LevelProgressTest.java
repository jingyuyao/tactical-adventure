package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LevelProgressTest {

  private static final Coordinate SPAWN1 = new Coordinate(2, 2);
  private static final Coordinate P1 = new Coordinate(10, 10);
  private static final Coordinate E1 = new Coordinate(3, 3);

  @Mock
  private GameSave gameSave;
  @Mock
  private LevelWorld levelWorld;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship player1;
  @Mock
  private Ship player2;
  @Mock
  private Ship enemy1;
  @Mock
  private Turn turn1;
  @Mock
  private Turn turn2;

  @Test
  public void fresh() {
    LevelProgress levelProgress = new LevelProgress();
    assertThat(levelProgress.getShips()).isEmpty();
    assertThat(levelProgress.getReservedPlayerShips()).isEmpty();
    assertThat(levelProgress.getTurn()).isEqualTo(new Turn());
  }

  @Test
  public void from_game_save_and_level_data() {
    when(gameSave.getPlayerShips()).thenReturn(ImmutableList.of(player1, player2));
    when(levelWorld.getPlayerSpawns()).thenReturn(ImmutableList.of(SPAWN1));
    when(levelWorld.getShips()).thenReturn(ImmutableMap.of(E1, enemy1));

    LevelProgress levelProgress = new LevelProgress(gameSave, levelWorld);

    assertThat(levelProgress.getShips()).containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(levelProgress.getReservedPlayerShips()).containsExactly(player2);
  }

  @Test
  public void update() {
    when(cell1.getCoordinate()).thenReturn(P1);
    when(cell2.getCoordinate()).thenReturn(E1);
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, player1, cell2, enemy1));
    when(worldState.getTurn()).thenReturn(turn1, turn2);

    LevelProgress levelProgress = new LevelProgress();
    levelProgress.update(world, worldState);

    assertThat(levelProgress.getShips()).containsExactly(P1, player1, E1, enemy1);
    assertThat(levelProgress.getTurn()).isSameAs(turn1);

    // Make sure the previous things are cleared
    levelProgress.update(world, worldState);
    assertThat(levelProgress.getShips()).containsExactly(P1, player1, E1, enemy1);
    assertThat(levelProgress.getTurn()).isSameAs(turn2);
  }
}