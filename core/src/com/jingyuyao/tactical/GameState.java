package com.jingyuyao.tactical;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.GameSave;
import com.jingyuyao.tactical.data.GameSaveManager;
import com.jingyuyao.tactical.data.LevelData;
import com.jingyuyao.tactical.data.LevelDataManager;
import com.jingyuyao.tactical.data.LevelMapManager;
import com.jingyuyao.tactical.data.LevelProgress;
import com.jingyuyao.tactical.data.LevelProgressManager;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameState {

  private final TacticalAdventure game;
  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;
  private final LevelDataManager levelDataManager;
  private final LevelMapManager levelMapManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final Model model;
  private final World world;

  @Inject
  GameState(
      TacticalAdventure game,
      GameSaveManager gameSaveManager,
      LevelProgressManager levelProgressManager,
      LevelDataManager levelDataManager,
      LevelMapManager levelMapManager,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model,
      World world) {
    this.game = game;
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    this.levelDataManager = levelDataManager;
    this.levelMapManager = levelMapManager;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    this.world = world;
  }

  public void play() {
    continueLevel();
  }

  public void reset() {
    levelProgressManager.removeSave();
  }

  void start() {
    game.goToPlayMenu();
  }

  void pause() {
    if (game.isAtWorldScreen()) {
      saveProgress();
    }
  }

  void finishLevel() {
    model.reset();
    levelProgressManager.removeSave();
    game.goToPlayMenu();
  }

  private void continueLevel() {
    GameSave gameSave = gameSaveManager.load();
    int level = gameSave.getCurrentLevel();

    LevelProgress levelProgress;
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();

    if (levelProgressOptional.isPresent()) {
      levelProgress = levelProgressOptional.get();
    } else {
      LevelData levelData = levelDataManager.load(level);
      levelProgress = new LevelProgress(gameSave, levelData);
      levelProgressManager.save(levelProgress);
    }

    Map<Coordinate, Terrain> terrainMap = levelMapManager.load(level, tiledMapRenderer);
    Map<Coordinate, Character> characterMap = levelProgress.getActiveCharacters();

    model.initialize(terrainMap, characterMap);
    game.goToWorldScreen();
  }

  private void saveProgress() {
    model.prepForSave();

    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();

    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException("An existing level progress file is needed for saving.");
    }

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world.getCharacterSnapshot());
    levelProgressManager.save(levelProgress);
  }
}
