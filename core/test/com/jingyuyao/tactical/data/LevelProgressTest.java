package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
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
  private LevelData levelData;
  @Mock
  private World world;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Enemy enemy1;

  @Test
  public void empty() {
    assertThat(new LevelProgress().getActiveCharacters()).isEmpty();
  }

  @Test
  public void from_game_save_and_level_data() {
    when(gameSave.getPlayers()).thenReturn(ImmutableList.of(player1, player2));
    when(levelData.getPlayerSpawns()).thenReturn(ImmutableList.of(SPAWN1));
    when(levelData.getEnemies()).thenReturn(ImmutableMap.of(E1, enemy1));

    LevelProgress levelProgress = new LevelProgress(gameSave, levelData);

    assertThat(levelProgress.getActiveCharacters()).containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(levelProgress.getInactivePlayers()).containsExactly(player2);
  }

  @Test
  public void update() {
    when(cell1.getCoordinate()).thenReturn(P1);
    when(cell1.hasPlayer()).thenReturn(true);
    when(cell1.getPlayer()).thenReturn(player1);
    when(cell2.getCoordinate()).thenReturn(E1);
    when(cell2.hasEnemy()).thenReturn(true);
    when(cell2.getEnemy()).thenReturn(enemy1);
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell1, cell2));

    LevelProgress levelProgress = new LevelProgress();
    levelProgress.update(world);

    assertThat(levelProgress.getActiveCharacters()).containsExactly(P1, player1, E1, enemy1);

    // Make sure the previous things are cleared
    levelProgress.update(world);
    assertThat(levelProgress.getActiveCharacters()).containsExactly(P1, player1, E1, enemy1);
  }
}