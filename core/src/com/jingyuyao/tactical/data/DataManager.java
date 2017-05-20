package com.jingyuyao.tactical.data;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Public api of all game data related functions.
 */
@Singleton
public class DataManager {

  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;
  private final LevelDataLoader levelDataLoader;
  private final LevelTerrainsLoader levelTerrainsLoader;
  private final ScriptLoader scriptLoader;

  @Inject
  DataManager(
      GameSaveManager gameSaveManager,
      LevelProgressManager levelProgressManager,
      LevelDataLoader levelDataLoader,
      LevelTerrainsLoader levelTerrainsLoader,
      ScriptLoader scriptLoader) {
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    this.levelDataLoader = levelDataLoader;
    this.levelTerrainsLoader = levelTerrainsLoader;
    this.scriptLoader = scriptLoader;
  }

  public GameSave loadCurrentSave() {
    return gameSaveManager.loadData();
  }

  public Optional<LevelProgress> loadCurrentProgress() {
    return levelProgressManager.load();
  }

  public boolean hasLevel(int level) {
    return levelDataLoader.hasLevel(level);
  }

  public void changeLevel(int level, World world, WorldState worldState) {
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();
    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException(
          "An existing level progress file is needed for changing levels.");
    }

    world.makeAllPlayerShipsControllable();

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world, worldState);

    GameSave gameSave = gameSaveManager.loadData();
    gameSave.setCurrentLevel(level);
    gameSave.update(levelProgress);
    gameSaveManager.saveData(gameSave);
    levelProgressManager.removeSave();
  }

  public void freshStart() {
    levelProgressManager.removeSave();
    gameSaveManager.removeSavedData();
  }

  public LoadedLevel loadCurrentLevel(OrthogonalTiledMapRenderer tiledMapRenderer) {
    GameSave gameSave = gameSaveManager.loadData();
    int level = gameSave.getCurrentLevel();

    LevelProgress levelProgress;
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();

    if (levelProgressOptional.isPresent()) {
      levelProgress = levelProgressOptional.get();
    } else {
      LevelWorld levelWorld = levelDataLoader.loadWorld(level);
      levelProgress = new LevelProgress(gameSave, levelWorld);
      levelProgressManager.save(levelProgress);
    }

    Map<Coordinate, Terrain> terrainMap = levelTerrainsLoader.load(level, tiledMapRenderer);
    Map<Coordinate, Ship> shipMap = levelProgress.getShips();
    Turn turn = levelProgress.getTurn();
    return new LoadedLevel(terrainMap, shipMap, turn, scriptLoader.load(level));
  }

  public void saveProgress(World world, WorldState worldState) {
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();

    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException("An existing level progress file is needed for saving.");
    }

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world, worldState);
    levelProgressManager.save(levelProgress);
  }

  public void removeProgress() {
    levelProgressManager.removeSave();
  }
}
