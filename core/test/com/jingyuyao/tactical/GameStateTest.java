package com.jingyuyao.tactical;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LoadedLevel;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
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
  private TacticalAdventure game;
  @Mock
  private DataManager dataManager;
  @Mock
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  @Mock
  private Model model;
  @Mock
  private World world;
  @Mock
  private LoadedLevel loadedLevel;
  @Mock
  private GameSave gameSave;

  private GameState gameState;

  @Before
  public void setUp() {
    gameState = new GameState(game, dataManager, tiledMapRenderer, model, world);
  }

  @Test
  public void play() {
    Map<Coordinate, Character> characterMap = new HashMap<>();
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    when(dataManager.loadCurrentLevel(tiledMapRenderer)).thenReturn(loadedLevel);
    when(loadedLevel.getCharacterMap()).thenReturn(characterMap);
    when(loadedLevel.getTerrainMap()).thenReturn(terrainMap);

    gameState.play();

    verify(model).initialize(terrainMap, characterMap);
    verify(game).goToWorldScreen();
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
  public void pause_at_world() {
    when(game.isAtWorldScreen()).thenReturn(true);

    gameState.pause();

    verify(model).prepForSave();
    verify(dataManager).saveProgress(world);
  }

  @Test
  public void pause_not_at_world() {
    when(game.isAtWorldScreen()).thenReturn(false);

    gameState.pause();

    verifyZeroInteractions(model);
    verifyZeroInteractions(dataManager);
  }

  @Test
  public void advance_level_has_level() {
    when(dataManager.loadCurrentSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(dataManager.hasLevel(3)).thenReturn(true);

    gameState.advanceLevel();

    InOrder inOrder = Mockito.inOrder(model, dataManager, game);
    inOrder.verify(model).prepForSave();
    inOrder.verify(dataManager).changeLevel(3, world);
    inOrder.verify(model).reset();
    inOrder.verify(game).goToPlayMenu();
  }

  @Test
  public void advance_level_no_leve() {
    when(dataManager.loadCurrentSave()).thenReturn(gameSave);
    when(gameSave.getCurrentLevel()).thenReturn(2);
    when(dataManager.hasLevel(3)).thenReturn(false);

    gameState.advanceLevel();

    verify(dataManager).freshStart();
    verify(model).reset();
    verify(game).goToPlayMenu();
  }

  @Test
  public void replay_level() {
    gameState.replayLevel();

    verify(dataManager).removeProgress();
    verify(model).reset();
    verify(game).goToPlayMenu();
  }
}