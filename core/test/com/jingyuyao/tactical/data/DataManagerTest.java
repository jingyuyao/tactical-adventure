package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

  private static final Coordinate SPAWN1 = new Coordinate(2, 2);
  private static final Coordinate E1 = new Coordinate(3, 3);

  @Mock
  private GameSaveManager gameSaveManager;
  @Mock
  private LevelProgressManager levelProgressManager;
  @Mock
  private LevelDataManager levelDataManager;
  @Mock
  private LevelMapManager levelMapManager;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelProgress levelProgress;
  @Mock
  private LevelData levelData;
  @Mock
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Enemy enemy1;
  @Captor
  private ArgumentCaptor<LevelProgress> levelProgressCaptor;

  private DataManager dataManager;

  @Before
  public void setUp() {
    dataManager =
        new DataManager(gameSaveManager, levelProgressManager, levelDataManager, levelMapManager);
  }

  @Test
  public void load_current_save() {
    when(gameSaveManager.load()).thenReturn(gameSave);

    assertThat(dataManager.loadCurrentSave()).isSameAs(gameSave);
  }

  @Test
  public void load_current_progress() {
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));

    assertThat(dataManager.loadCurrentProgress()).hasValue(levelProgress);
  }

  @Test
  public void has_level() {
    when(levelDataManager.hasLevel(2)).thenReturn(true);

    assertThat(dataManager.hasLevel(2)).isTrue();
  }

  @Test
  public void change_level() {
    when(gameSaveManager.load()).thenReturn(gameSave);
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));

    dataManager.changeLevel(2);

    verify(gameSave).setCurrentLevel(2);
    verify(gameSave).update(levelProgress);
    verify(gameSaveManager).save(gameSave);
    verify(levelProgressManager).removeSave();
  }

  @Test
  public void fresh_start() {
    dataManager.freshStart();

    verify(gameSaveManager).removeSave();
    verify(levelProgressManager).removeSave();
  }

  @Test
  public void load_level_has_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Character> characterMap = new HashMap<>();
    when(gameSaveManager.load()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));
    when(levelMapManager.load(2, tiledMapRenderer)).thenReturn(terrainMap);
    when(levelProgress.getActiveCharacters()).thenReturn(characterMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getCharacterMap()).isSameAs(characterMap);
  }

  @Test
  public void load_level_no_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    when(gameSaveManager.load()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(levelProgressManager.load()).thenReturn(Optional.<LevelProgress>absent());
    when(levelDataManager.load(2)).thenReturn(levelData);
    when(gameSave.getPlayers()).thenReturn(ImmutableList.of(player1, player2));
    when(levelData.getPlayerSpawns()).thenReturn(ImmutableList.of(SPAWN1));
    when(levelData.getEnemies()).thenReturn(ImmutableMap.of(E1, enemy1));
    when(levelMapManager.load(2, tiledMapRenderer)).thenReturn(terrainMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    verify(levelProgressManager).save(levelProgressCaptor.capture());
    assertThat(levelProgressCaptor.getValue().getInactivePlayers()).containsExactly(player2);
    assertThat(levelProgressCaptor.getValue().getActiveCharacters())
        .containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getCharacterMap()).containsExactly(SPAWN1, player1, E1, enemy1);
  }

  @Test
  public void save_progress() {
    Iterable<Cell> cells = ImmutableList.of();
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));

    dataManager.saveProgress(cells);

    verify(levelProgress).update(cells);
    verify(levelProgressManager).save(levelProgress);
  }

  @Test
  public void remove_progress() {
    dataManager.removeProgress();

    verify(levelProgressManager).removeSave();
  }
}