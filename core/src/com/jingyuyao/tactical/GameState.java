package com.jingyuyao.tactical;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.data.LoadedLevel;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameState {

  private final TacticalAdventure game;
  private final DataManager dataManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final Model model;
  private final World world;

  @Inject
  GameState(
      TacticalAdventure game,
      DataManager dataManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model,
      World world) {
    this.game = game;
    this.dataManager = dataManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    this.world = world;
  }

  public void play() {
    LoadedLevel loadedLevel = dataManager.loadCurrentLevel(tiledMapRenderer);
    model.initialize(loadedLevel.getTerrainMap(), loadedLevel.getCharacterMap());
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
      dataManager.saveProgress(world.getCharacterSnapshot());
    }
  }

  void finishLevel() {
    model.reset();
    dataManager.removeProgress();
    game.goToPlayMenu();
  }
}
