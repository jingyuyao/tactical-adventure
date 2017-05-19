package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
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
  private GameDataManager gameDataManager;
  @Mock
  private LevelProgressManager levelProgressManager;
  @Mock
  private LevelDataLoader levelDataLoader;
  @Mock
  private LevelTerrainsLoader levelTerrainsLoader;
  @Mock
  private ScriptLoader scriptLoader;
  @Mock
  private GameData gameData;
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
  private Ship player1;
  @Mock
  private Ship player2;
  @Mock
  private Ship enemy1;
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
            gameDataManager, levelProgressManager, levelDataLoader, levelTerrainsLoader,
            scriptLoader);
  }

  @Test
  public void load_current_save() {
    when(gameDataManager.loadData()).thenReturn(gameData);

    assertThat(dataManager.loadCurrentSave()).isSameAs(gameData);
  }

  @Test
  public void has_level() {
    when(levelDataLoader.hasLevel(2)).thenReturn(true);

    assertThat(dataManager.hasLevel(2)).isTrue();
  }

  @Test
  public void change_level() {
    when(gameDataManager.loadData()).thenReturn(gameData);
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));

    dataManager.changeLevel(2, world, worldState);

    InOrder inOrder =
        Mockito.inOrder(world, gameData, levelProgress, gameDataManager, levelProgressManager);

    inOrder.verify(world).resetPlayerShipStats();
    inOrder.verify(levelProgress).update(world, worldState);
    inOrder.verify(gameData).setCurrentLevel(2);
    inOrder.verify(gameData).update(levelProgress);
    inOrder.verify(gameDataManager).saveData(gameData);
    inOrder.verify(levelProgressManager).removeSave();
  }

  @Test
  public void fresh_start() {
    dataManager.freshStart();

    verify(gameDataManager).removeSavedData();
    verify(levelProgressManager).removeSave();
  }

  @Test
  public void load_level_has_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    when(gameDataManager.loadData()).thenReturn(gameData);
    when(gameData.getCurrentLevel()).thenReturn(2);
    when(levelProgressManager.load()).thenReturn(Optional.of(levelProgress));
    when(levelTerrainsLoader.load(2, tiledMapRenderer)).thenReturn(terrainMap);
    when(levelProgress.getShips()).thenReturn(shipMap);
    when(levelProgress.getTurn()).thenReturn(turn);
    when(scriptLoader.load(2)).thenReturn(script);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getShipMap()).isSameAs(shipMap);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
  }

  @Test
  public void load_level_no_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    when(gameDataManager.loadData()).thenReturn(gameData);
    when(gameData.getCurrentLevel()).thenReturn(2);
    when(levelProgressManager.load()).thenReturn(Optional.<LevelProgress>absent());
    when(levelDataLoader.loadInit(2)).thenReturn(levelData);
    when(gameData.getPlayerShips()).thenReturn(ImmutableList.of(player1, player2));
    when(levelData.getPlayerSpawns()).thenReturn(ImmutableList.of(SPAWN1));
    when(levelData.getShips()).thenReturn(ImmutableMap.of(E1, enemy1));
    when(levelTerrainsLoader.load(2, tiledMapRenderer)).thenReturn(terrainMap);
    when(scriptLoader.load(2)).thenReturn(script);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    verify(levelProgressManager).save(levelProgressCaptor.capture());
    assertThat(levelProgressCaptor.getValue().getReservedPlayerShips()).containsExactly(player2);
    assertThat(levelProgressCaptor.getValue().getShips())
        .containsExactly(SPAWN1, player1, E1, enemy1);
    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getShipMap()).containsExactly(SPAWN1, player1, E1, enemy1);
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