package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
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
import com.jingyuyao.tactical.view.WorldScreen;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameState {

  private final Game game;
  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;
  private final LevelDataManager levelDataManager;
  private final LevelMapManager levelMapManager;
  private final WorldScreen worldScreen;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;
  private final Model model;
  private final World world;

  @Inject
  GameState(
      Game game,
      GameSaveManager gameSaveManager,
      LevelProgressManager levelProgressManager,
      LevelDataManager levelDataManager,
      LevelMapManager levelMapManager,
      WorldScreen worldScreen,
      OrthogonalTiledMapRenderer tiledMapRenderer,
      Model model,
      World world) {
    this.game = game;
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    this.levelDataManager = levelDataManager;
    this.levelMapManager = levelMapManager;
    this.worldScreen = worldScreen;
    this.tiledMapRenderer = tiledMapRenderer;
    this.model = model;
    this.world = world;
  }

  void pause() {
    saveProgress();
  }

  void dispose() {
    worldScreen.dispose();
  }

  void continueLevel() {
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
    game.setScreen(worldScreen);
  }

  void replay() {
    model.reset();
    levelProgressManager.removeSave();
    continueLevel();
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
