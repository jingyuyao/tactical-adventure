package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.LoadedLevel;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
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
  private final Model model;
  private final World world;
  private final WorldState worldState;

  @Inject
  GameState(
      Application application,
      TacticalAdventure game,
      DataManager dataManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model,
      World world,
      WorldState worldState) {
    this.application = application;
    this.game = game;
    this.dataManager = dataManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    this.world = world;
    this.worldState = worldState;
  }

  public void play() {
    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);
    model.initialize(
        loadedLevel.getTerrainMap(),
        loadedLevel.getCharacterMap(),
        loadedLevel.getTurn(),
        loadedLevel.getScript());
    game.goToWorldScreen();
  }

  public void reset() {
    dataManager.removeProgress();
  }

  void start() {
    game.goToPlayMenu();
  }

  void pause() {
    if (game.isAtWorldScreen()) {
      model.prepForSave();
      dataManager.saveProgress(world, worldState);
    }
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    GameSave gameSave = dataManager.loadCurrentSave();
    int nextLevel = gameSave.getCurrentLevel() + 1;
    if (dataManager.hasLevel(nextLevel)) {
      model.prepForSave();
      dataManager.changeLevel(nextLevel, world, worldState);
    } else {
      dataManager.freshStart();
    }
    model.reset();
    game.goToPlayMenu();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    dataManager.removeProgress();
    model.reset();
    game.goToPlayMenu();
  }

  @Subscribe
  void deadEvent(DeadEvent deadEvent) {
    application.log("GameStateSubscriber", deadEvent.getEvent().toString());
  }
}
