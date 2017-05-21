package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LoadedLevel;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
public class GameState {

  private final Application application;
  private final TacticalAdventure game;
  private final DataManager dataManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final World world;
  private final WorldState worldState;

  @Inject
  GameState(
      Application application,
      TacticalAdventure game,
      DataManager dataManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      World world,
      WorldState worldState) {
    this.application = application;
    this.game = game;
    this.dataManager = dataManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.world = world;
    this.worldState = worldState;
  }

  public void play() {
    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);
    // Order is important: world must be populated before we go to the screen since the screen
    // needs the world size, state need to start after we are in the screen so all the UI can
    // receive events properly
    world.initialize(loadedLevel.getTerrainMap(), loadedLevel.getShipMap());
    game.goToWorldScreen();
    worldState.initialize(loadedLevel.getTurn(), loadedLevel.getScript());
  }

  public void reset() {
    dataManager.removeLevelProgress();
  }

  void start() {
    game.goToPlayMenu();
  }

  @Subscribe
  void save(Save save) {
    dataManager.saveLevelProgress(world, worldState);
  }

  @Subscribe
  void levelWon(LevelWon levelWon) {
    GameSave gameSave = dataManager.loadGameSave();
    int nextLevel = gameSave.getCurrentLevel() + 1;
    if (dataManager.hasLevel(nextLevel)) {
      dataManager.changeLevel(nextLevel, world);
    } else {
      dataManager.freshStart();
    }
    world.reset();
    worldState.reset();
    game.goToPlayMenu();
  }

  @Subscribe
  void levelLost(LevelLost levelLost) {
    dataManager.removeLevelProgress();
    world.reset();
    worldState.reset();
    game.goToPlayMenu();
  }

  @Subscribe
  void deadEvent(DeadEvent deadEvent) {
    application.log("GameStateSubscriber", deadEvent.getEvent().toString());
  }
}
