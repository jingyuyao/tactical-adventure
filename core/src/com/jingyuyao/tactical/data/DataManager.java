package com.jingyuyao.tactical.data;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;
  private final LevelDataManager levelDataManager;
  private final LevelMapManager levelMapManager;

  @Inject
  DataManager(
      GameSaveManager gameSaveManager,
      LevelProgressManager levelProgressManager,
      LevelDataManager levelDataManager,
      LevelMapManager levelMapManager) {
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    this.levelDataManager = levelDataManager;
    this.levelMapManager = levelMapManager;
  }

  public GameSave loadCurrentSave() {
    return gameSaveManager.load();
  }

  public boolean hasLevel(int level) {
    return levelDataManager.hasLevel(level);
  }

  public void changeLevel(int level, World world) {
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();
    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException(
          "An existing level progress file is needed for changing levels.");
    }

    world.fullHealPlayers();

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world);

    GameSave gameSave = gameSaveManager.load();
    gameSave.setCurrentLevel(level);
    gameSave.update(levelProgress);
    gameSaveManager.save(gameSave);
    levelProgressManager.removeSave();
  }

  public void freshStart() {
    levelProgressManager.removeSave();
    gameSaveManager.removeSave();
  }

  public LoadedLevel loadCurrentLevel(OrthogonalTiledMapRenderer tiledMapRenderer) {
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
    return new LoadedLevel(terrainMap, characterMap);
  }

  public void saveProgress(World world) {
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();

    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException("An existing level progress file is needed for saving.");
    }

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world);
    levelProgressManager.save(levelProgress);
  }

  public void removeProgress() {
    levelProgressManager.removeSave();
  }

  public String getInfo() {
    String progress = "No progress";
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();
    if (levelProgressOptional.isPresent()) {
      LevelProgress levelProgress = levelProgressOptional.get();
      int activePlayers = levelProgress.getActivePlayers().size();
      int activeEnemies = levelProgress.getActiveEnemies().size();
      progress =
          String.format(
              Locale.US,
              "%d player characters, %d enemies remaining", activePlayers, activeEnemies);
    }

    GameSave gameSave = loadCurrentSave();
    int level = gameSave.getCurrentLevel();
    return String.format(Locale.US, "Current level: %d\n%s", level, progress);
  }
}