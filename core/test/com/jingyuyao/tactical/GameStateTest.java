package com.jingyuyao.tactical;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.google.common.eventbus.DeadEvent;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LevelSave;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.List;
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
public class GameStateTest {

  @Mock
  private TacticalAdventure game;
  @Mock
  private Application application;
  @Mock
  private BackgroundService backgroundService;
  @Mock
  private DataManager dataManager;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private LevelSave levelSave;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Mock
  private GameSave gameSave;
  @Mock
  private Save save;
  @Mock
  private LevelWon levelWon;
  @Mock
  private LevelLost levelLost;
  @Mock
  private DeadEvent deadEvent;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private GameState gameState;

  @Before
  public void setUp() {
    gameState = new GameState(game, application, backgroundService, dataManager, world, worldState);
  }

  @Test
  public void play() {
    List<Cell> worldCells = new ArrayList<>();
    List<Ship> shipList = new ArrayList<>();
    when(dataManager.loadCurrentLevel()).thenReturn(levelSave);
    when(levelSave.getWorldCells()).thenReturn(worldCells);
    when(levelSave.getInactiveShips()).thenReturn(shipList);
    when(levelSave.getTurn()).thenReturn(turn);
    when(levelSave.getScript()).thenReturn(script);
    when(levelSave.getLevel()).thenReturn(2);

    gameState.play();

    InOrder inOrder = Mockito.inOrder(world, worldState, game);
    inOrder.verify(world).initialize(2, worldCells, shipList);
    inOrder.verify(game).goToWorldScreen();
    inOrder.verify(worldState).initialize(turn, script);
  }

  @Test
  public void start() {
    gameState.start();

    verify(game).goToPlayMenu();
  }

  @Test
  public void save() {
    gameState.save(save);

    verify(backgroundService).submit(runnableCaptor.capture(), runnableCaptor.capture());
    verifyZeroInteractions(dataManager);
    verifyZeroInteractions(save);

    runnableCaptor.getAllValues().get(0).run();
    verify(dataManager).saveLevelProgress(world, worldState);

    runnableCaptor.getAllValues().get(1).run();
    verify(save).complete();
  }

  @Test
  public void level_complete_has_level() {
    when(dataManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(dataManager.hasLevel(3)).thenReturn(true);

    gameState.levelWon(levelWon);

    InOrder inOrder = Mockito.inOrder(world, worldState, dataManager, game);
    inOrder.verify(dataManager).changeLevel(3, world);
    inOrder.verify(world).reset();
    inOrder.verify(worldState).reset();
    inOrder.verify(game).goToPlayMenu();
  }

  @Test
  public void level_complete_no_level() {
    when(dataManager.loadGameSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(dataManager.hasLevel(3)).thenReturn(false);

    gameState.levelWon(levelWon);

    verify(dataManager).freshStart();
    verify(world).reset();
    verify(worldState).reset();
    verify(game).goToPlayMenu();
  }

  @Test
  public void level_failed() {
    gameState.levelLost(levelLost);

    verify(dataManager).removeLevelProgress();
    verify(world).reset();
    verify(worldState).reset();
    verify(game).goToPlayMenu();
  }

  @Test
  public void dead_event() {
    Object object = new Object();
    when(deadEvent.getEvent()).thenReturn(object);

    gameState.deadEvent(deadEvent);

    verify(application).log(anyString(), eq(object.toString()));
  }
}