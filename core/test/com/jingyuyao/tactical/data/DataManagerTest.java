package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
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
  private Turn turn;
  @Mock
  private Script script;
  @Captor
  private ArgumentCaptor<LevelSave> levelSaveCaptor;

  private DataManager dataManager;

  @Before
  public void setUp() {
    dataManager = new DataManager(saveManager, levelLoader);
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
    when(world.getLevel()).thenReturn(2);
    when(world.getWorldCells()).thenReturn(ImmutableList.of(cell1, cell2));
    when(world.getInactiveShips()).thenReturn(ImmutableList.of(ship1));
    when(worldState.getTurn()).thenReturn(turn);
    when(worldState.getScript()).thenReturn(script);

    dataManager.saveLevelProgress(world, worldState);

    verify(saveManager).save(levelSaveCaptor.capture());
    LevelSave levelSave = levelSaveCaptor.getValue();
    assertThat(levelSave.getLevel()).isEqualTo(2);
    assertThat(levelSave.getWorldCells()).containsExactly(cell1, cell2);
    assertThat(levelSave.getInactiveShips()).containsExactly(ship1);
    assertThat(levelSave.getTurn()).isSameAs(turn);
    assertThat(levelSave.getScript()).isSameAs(script);
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
    when(saveManager.loadLevelSave()).thenReturn(Optional.of(levelSave));

    assertThat(dataManager.loadLevelSave()).hasValue(levelSave);
  }

  @Test
  public void load_level_no_progress() {
    ImmutableList<Ship> playerShips = ImmutableList.of();
    when(saveManager.loadGameSave()).thenReturn(gameSave);
    when(saveManager.loadLevelSave()).thenReturn(Optional.<LevelSave>absent());
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(gameSave.getPlayerShips()).thenReturn(playerShips);
    when(levelLoader.createNewSave(2, playerShips)).thenReturn(levelSave);

    assertThat(dataManager.loadCurrentLevel()).isSameAs(levelSave);

    verify(saveManager).save(levelSave);
  }
}