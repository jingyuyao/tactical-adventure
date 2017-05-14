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
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
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
  private ScriptLoader scriptLoader;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelProgress levelProgress;
  @Mock
  private LevelData levelData;
  @Mock
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Enemy enemy1;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Captor
  private ArgumentCaptor<LevelProgress> levelProgressCaptor;

  private DataManager dataManager;

  @Before
  public void setUp() {
    dataManager =
        new DataManager(
            gameSaveManager, levelProgressManager, levelDataManager, levelMapManager, scriptLoader);
  }

  @Test
  public void load_current_save() {
    when(gameSaveManager.load()).thenReturn(gameSave);

    assertThat(dataManager.loadCurrentSave()).isSameAs(gameSave);
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

    dataManager.changeLevel(2, world, worldState);

    InOrder inOrder =
        Mockito.inOrder(world, gameSave, levelProgress, gameSaveManager, levelProgressManager);

    inOrder.verify(world).fullHealPlayers();
    inOrder.verify(levelProgress).update(world, worldState);
    inOrder.verify(gameSave).setCurrentLevel(2);
    inOrder.verify(gameSave).update(levelProgress);
    inOrder.verify(gameSaveManager).save(gameSave);
    inOrder.verify(levelProgressManager).removeSave();
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
    when(levelProgress.getTurn()).thenReturn(turn);
    when(scriptLoader.load(2)).thenReturn(script);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getCharacterMap()).isSameAs(characterMap);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
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
    when(scriptLoader.load(2)).thenReturn(script);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    verify(levelProgressManager).save(levelProgressCaptor.capture());
    assertThat(levelProgressCaptor.getValue().getInactivePlayers()).containsExactly(player2);
    assertThat(levelProgressCaptor.getValue().getActiveCharacters())
        .containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getCharacterMap()).containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(loadedLevel.getTurn()).isEqualTo(new Turn());
    assertThat(loadedLevel.getScript()).isSameAs(script);
  }

  @Test
  public void save_progress() {
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));

    dataManager.saveProgress(world, worldState);

    verify(levelProgress).update(world, worldState);
    verify(levelProgressManager).save(levelProgress);
  }

  @Test
  public void remove_progress() {
    dataManager.removeProgress();

    verify(levelProgressManager).removeSave();
  }
}