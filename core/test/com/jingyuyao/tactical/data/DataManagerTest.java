package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Ship ship3;
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
  public void load_level_save() {
    when(saveManager.loadLevelSave()).thenReturn(Optional.of(levelSave));

    assertThat(dataManager.loadLevelSave()).hasValue(levelSave);
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
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(world.getInactiveShips()).thenReturn(ImmutableList.of(ship3));
    when(cell1.getCoordinate()).thenReturn(C0_0);
    when(cell2.getCoordinate()).thenReturn(C0_1);
    when(worldState.getTurn()).thenReturn(turn);
    when(worldState.getScript()).thenReturn(script);

    dataManager.saveLevelProgress(world, worldState);

    verify(saveManager).save(levelSaveCaptor.capture());
    LevelSave levelSave = levelSaveCaptor.getValue();
    assertThat(levelSave.getTurn()).isSameAs(turn);
    assertThat(levelSave.getScript()).isSameAs(script);
    assertThat(levelSave.getActiveShips()).containsExactly(C0_0, ship1, C0_1, ship2);
    assertThat(levelSave.getInactiveShips()).containsExactly(ship3);
  }

  @Test
  public void remove_level_progress() {
    dataManager.removeLevelProgress();

    verify(saveManager).removeLevelSave();
  }

  @Test
  public void change_level() {
    when(saveManager.loadGameSave()).thenReturn(gameSave);

    dataManager.changeLevel(2, world);

    InOrder inOrder = Mockito.inOrder(world, gameSave, levelSave, saveManager);

    inOrder.verify(world).makeAllPlayerShipsControllable();
    inOrder.verify(gameSave).setCurrentLevel(2);
    inOrder.verify(gameSave).replacePlayerShipsFrom(world);
    inOrder.verify(saveManager).save(gameSave);
    inOrder.verify(saveManager).removeLevelSave();
  }

  @Test
  public void load_level_has_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    List<Ship> shipList = new ArrayList<>();
    when(saveManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(saveManager.loadLevelSave()).thenReturn(Optional.of(levelSave));
    when(levelSave.getActiveShips()).thenReturn(shipMap);
    when(levelSave.getInactiveShips()).thenReturn(shipList);
    when(levelSave.getTurn()).thenReturn(turn);
    when(levelSave.getScript()).thenReturn(script);
    when(levelTerrainsLoader.load(2)).thenReturn(terrainMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel();

    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getActiveShips()).isSameAs(shipMap);
    assertThat(loadedLevel.getInactiveShips()).isSameAs(shipList);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
    assertThat(loadedLevel.getLevel()).isEqualTo(2);
  }

  @Test
  public void load_level_no_progress() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    List<Ship> shipList = new ArrayList<>();
    when(saveManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(saveManager.loadLevelSave()).thenReturn(Optional.<LevelSave>absent());
    when(levelLoader.createNewSave(2, gameSave)).thenReturn(levelSave);
    when(levelSave.getActiveShips()).thenReturn(shipMap);
    when(levelSave.getInactiveShips()).thenReturn(shipList);
    when(levelSave.getTurn()).thenReturn(turn);
    when(levelSave.getScript()).thenReturn(script);
    when(levelTerrainsLoader.load(2)).thenReturn(terrainMap);

    LoadedLevel loadedLevel = dataManager.loadCurrentLevel();

    verify(saveManager).save(levelSave);
    assertThat(loadedLevel.getTerrainMap()).isSameAs(terrainMap);
    assertThat(loadedLevel.getActiveShips()).isSameAs(shipMap);
    assertThat(loadedLevel.getInactiveShips()).isSameAs(shipList);
    assertThat(loadedLevel.getTurn()).isSameAs(turn);
    assertThat(loadedLevel.getScript()).isSameAs(script);
    assertThat(loadedLevel.getLevel()).isEqualTo(2);
  }
}