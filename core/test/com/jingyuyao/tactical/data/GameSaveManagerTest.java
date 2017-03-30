package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameSaveManagerTest {

  private static final String MAIN_SAVE = "main.save.json";
  private static final String START_SAVE = "main.json";
  private static final String DATA = "hello world!";
  private static final int LEVEL_ID = 10;

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private GameSave gameSave;
  @Mock
  private FileHandle fileHandle1;
  @Mock
  private FileHandle fileHandle2;
  @Mock
  private LevelData levelData;
  @Mock
  private Enemy enemy1;
  @Mock
  private Enemy enemy2;
  @Mock
  private Enemy enemy3;
  @Mock
  private Enemy cEnemy1;
  @Mock
  private Enemy cEnemy2;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Player player3;
  @Mock
  private Player cPlayer1;
  @Mock
  private Player cPlayer2;

  private GameSaveManager gameSaveManager;

  @Before
  public void setUp() {
    gameSaveManager = new GameSaveManager(dataConfig, myGson, files);
  }

  @Test
  public void load_main_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(gameSaveManager.load()).isSameAs(gameSave);
  }

  @Test
  public void load_start_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(dataConfig.getStartSaveFileName()).thenReturn(START_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(files.local(START_SAVE)).thenReturn(fileHandle2);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(gameSaveManager.load()).isSameAs(gameSave);
  }

  @Test
  public void save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(myGson.toJson(gameSave)).thenReturn(DATA);

    gameSaveManager.save(gameSave);

    verify(fileHandle1).writeString(DATA, false);
  }

  @Test
  public void load_level() {
    Map<Coordinate, Enemy> enemyMap = new HashMap<>();
    Coordinate coordinate1 = new Coordinate(2, 3);
    Coordinate coordinate2 = new Coordinate(3, 4);
    Coordinate coordinate3 = new Coordinate(10, 10);
    enemyMap.put(coordinate1, enemy1);
    enemyMap.put(coordinate2, enemy2);
    List<Coordinate> spawns = ImmutableList.of(coordinate3);
    when(levelData.getId()).thenReturn(LEVEL_ID);
    when(levelData.getEnemies()).thenReturn(enemyMap);
    when(levelData.getPlayerSpawns()).thenReturn(spawns);
    List<Player> startingPlayers = ImmutableList.of(player1, player2);
    List<Player> inactivePlayers = new ArrayList<>();
    inactivePlayers.add(player1);
    Map<Coordinate, Player> activePlayers = new HashMap<>();
    activePlayers.put(coordinate3, player3);
    Map<Coordinate, Enemy> activeEnemies = new HashMap<>();
    activeEnemies.put(coordinate2, enemy3);
    when(gameSave.getStartingPlayers()).thenReturn(startingPlayers);
    when(gameSave.getInactivePlayers()).thenReturn(inactivePlayers);
    when(gameSave.getActivePlayers()).thenReturn(activePlayers);
    when(gameSave.getActiveEnemies()).thenReturn(activeEnemies);
    when(myGson.deepCopy(eq(player1), Mockito.<Class<?>>any())).thenReturn(cPlayer1);
    when(myGson.deepCopy(eq(player2), Mockito.<Class<?>>any())).thenReturn(cPlayer2);
    when(myGson.deepCopy(eq(enemy1), Mockito.<Class<?>>any())).thenReturn(cEnemy1);
    when(myGson.deepCopy(eq(enemy2), Mockito.<Class<?>>any())).thenReturn(cEnemy2);

    gameSaveManager.loadLevel(gameSave, levelData);

    verify(gameSave).setCurrentLevel(LEVEL_ID);
    verify(gameSave).setInProgress(true);
    assertThat(inactivePlayers).containsExactly(cPlayer2);
    assertThat(activePlayers).containsExactly(coordinate3, cPlayer1);
    assertThat(activeEnemies).containsExactly(coordinate1, cEnemy1, coordinate2, cEnemy2);
  }

  @Test
  public void remove_level_progress() {
    Coordinate coordinate1 = new Coordinate(2, 3);
    List<Player> inactivePlayers = new ArrayList<>();
    inactivePlayers.add(player2);
    Map<Coordinate, Player> activePlayers = new HashMap<>();
    activePlayers.put(coordinate1, player1);
    Map<Coordinate, Enemy> activeEnemies = new HashMap<>();
    activeEnemies.put(coordinate1, enemy1);
    when(gameSave.getInactivePlayers()).thenReturn(inactivePlayers);
    when(gameSave.getActivePlayers()).thenReturn(activePlayers);
    when(gameSave.getActiveEnemies()).thenReturn(activeEnemies);

    gameSaveManager.removeLevelProgress(gameSave);

    verify(gameSave).setInProgress(false);
    assertThat(inactivePlayers).isEmpty();
    assertThat(activePlayers).isEmpty();
    assertThat(activeEnemies).isEmpty();
  }
}