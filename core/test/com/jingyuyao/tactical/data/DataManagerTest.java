package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
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
  private SaveManager saveManager;
  @Mock
  private LevelLoader levelLoader;
  @Mock
  private LevelTerrainsLoader levelTerrainsLoader;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelSave levelSave;
  @Mock
  private OrthogonalTiledMapRenderer tiledMapRenderer;
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
  private Ship enemy1;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Captor
  private ArgumentCaptor<LevelSave> levelSaveCaptor;

  private DataManager dataManager;

  @Before
  public void setUp() {
    dataManager = new DataManager(saveManager, levelLoader, levelTerrainsLoader);
  }

  @Test
  public void load_current_save() {
    when(saveManager.loadGameSave()).thenReturn(gameSave);

    assertThat(dataManager.loadGameSave()).isSameAs(gameSave);
  }

  @Test
  public void has_level() {
    when(levelLoader.hasLevel(2)).thenReturn(true);

    assertThat(dataManager.hasLevel(2)).isTrue();
  }

  @Test
  public void fresh_start() {
    dataManager.freshStart();

    verify(saveManager).removeGameSave();
    verify(saveManager).removeLevelSave();
  }

  @Test
  public void save_level_progress() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, player1, cell2, enemy1));
    when(cell1.getCoordinate()).thenReturn(SPAWN1);
    when(cell2.getCoordinate()).thenReturn(E1);
    when(worldState.getTurn()).thenReturn(turn);
    when(worldState.getScript()).thenReturn(script);

    dataManager.saveLevelProgress(world, worldState);

    verify(saveManager).save(levelSaveCaptor.capture());
    LevelSave levelSave = levelSaveCaptor.getValue();
    assertThat(levelSave.getTurn()).isSameAs(turn);
    assertThat(levelSave.getScript()).isSameAs(script);
    assertThat(levelSave.getShips()).containsExactly(SPAWN1, player1, E1, enemy1);
  }

  @Test
  public void remove_level_progress() {
    when(saveManager.loadGameSave()).thenReturn(gameSave);

    dataManager.removeLevelProgress();

    verify(gameSave).deactivateShips();
    verify(saveManager).save(gameSave);
    verify(saveManager).removeLevelSave();
  }

  @Test
  public void change_level() {
    when(saveManager.loadGameSave()).thenReturn(gameSave);

    dataManager.changeLevel(2, world);

    InOrder inOrder = Mockito.inOrder(world, gameSave, levelSave, saveManager);

    inOrder.verify(world).makeAllPlayerShipsControllable();
    inOrder.verify(gameSave).setCurrentLevel(2);
    inOrder.verify(gameSave).replaceActiveShipsFrom(world);
    inOrder.verify(gameSave).deactivateShips();
    inOrder.verify(saveManager).save(gameSave);
    inOrder.verify(saveManager).removeLevelSave();
  }

  @Test
  public void load_level_has_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    when(saveManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(saveManager.loadLevelSave()).thenReturn(Optional.of(levelSave));
    when(levelSave.getShips()).thenReturn(shipMap);
    when(levelSave.getTurn()).thenReturn(turn);
    when(levelSave.getScript()).thenReturn(script);
    when(levelTerrainsLoader.load(2, tiledMapRenderer)).thenReturn(terrainMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getShipMap()).isSameAs(shipMap);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
  }

  @Test
  public void load_level_no_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    when(saveManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(saveManager.loadLevelSave()).thenReturn(Optional.<LevelSave>absent());
    when(levelLoader.createNewSave(2, gameSave)).thenReturn(levelSave);
    when(levelSave.getShips()).thenReturn(shipMap);
    when(levelSave.getTurn()).thenReturn(turn);
    when(levelSave.getScript()).thenReturn(script);
    when(levelTerrainsLoader.load(2, tiledMapRenderer)).thenReturn(terrainMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);

    verify(saveManager).save(gameSave);
    verify(saveManager).save(levelSave);
    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getShipMap()).isSameAs(shipMap);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
  }
}