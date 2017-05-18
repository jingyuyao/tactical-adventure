package com.jingyuyao.tactical;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.eventbus.DeadEvent;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameData;
import com.jingyuyao.tactical.data.LoadedLevel;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Save;
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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameStateTest {

  @Mock
  private Application application;
  @Mock
  private TacticalAdventure game;
  @Mock
  private DataManager dataManager;
  @Mock
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private LoadedLevel loadedLevel;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Mock
  private GameData gameData;
  @Mock
  private Save save;
  @Mock
  private LevelWon levelWon;
  @Mock
  private LevelLost levelLost;
  @Mock
  private DeadEvent deadEvent;

  private GameState gameState;

  @Before
  public void setUp() {
    gameState = new GameState(application, game, dataManager, tiledMapRenderer, world, worldState);
  }

  @Test
  public void play() {
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    when(dataManager.loadCurrentLevel(tiledMapRenderer)).thenReturn(loadedLevel);
    when(loadedLevel.getShipMap()).thenReturn(shipMap);
    when(loadedLevel.getTerrainMap()).thenReturn(terrainMap);
    when(loadedLevel.getTurn()).thenReturn(turn);
    when(loadedLevel.getScript()).thenReturn(script);

    gameState.play();

    InOrder inOrder = Mockito.inOrder(world, worldState, game);
    inOrder.verify(world).initialize(terrainMap, shipMap);
    inOrder.verify(game).goToWorldScreen();
    inOrder.verify(worldState).initialize(turn, script);
  }

  @Test
  public void reset() {
    gameState.reset();

    verify(dataManager).removeProgress();
  }

  @Test
  public void start() {
    gameState.start();

    verify(game).goToPlayMenu();
  }

  @Test
  public void save() {
    gameState.save(save);

    verify(dataManager).saveProgress(world, worldState);
  }

  @Test
  public void level_complete_has_level() {
    when(dataManager.loadCurrentSave()).thenReturn(gameData);
    when(gameData.getCurrentLevel()).thenReturn(2);
    when(dataManager.hasLevel(3)).thenReturn(true);

    gameState.levelWon(levelWon);

    InOrder inOrder = Mockito.inOrder(world, worldState, dataManager, game);
    inOrder.verify(dataManager).changeLevel(3, world, worldState);
    inOrder.verify(world).reset();
    inOrder.verify(worldState).reset();
    inOrder.verify(game).goToPlayMenu();
  }

  @Test
  public void level_complete_no_level() {
    when(dataManager.loadCurrentSave()).thenReturn(gameData);
    when(gameData.getCurrentLevel()).thenReturn(2);
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

    verify(dataManager).removeProgress();
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