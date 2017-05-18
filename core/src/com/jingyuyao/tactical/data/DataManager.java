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

@Singleton
public class DataManager {

  private final GameSaveManager gameSaveManager;
  private final LevelProgressManager levelProgressManager;
  private final LevelInitLoader levelInitLoader;
  private final LevelTerrainsLoader levelTerrainsLoader;
  private final ScriptLoader scriptLoader;

  @Inject
  DataManager(
      GameSaveManager gameSaveManager,
      LevelProgressManager levelProgressManager,
      LevelInitLoader levelInitLoader,
      LevelTerrainsLoader levelTerrainsLoader,
      ScriptLoader scriptLoader) {
    this.gameSaveManager = gameSaveManager;
    this.levelProgressManager = levelProgressManager;
    this.levelInitLoader = levelInitLoader;
    this.levelTerrainsLoader = levelTerrainsLoader;
    this.scriptLoader = scriptLoader;
  }

  public GameSave loadCurrentSave() {
    return gameSaveManager.load();
  }

  public Optional<LevelProgress> loadCurrentProgress() {
    return levelProgressManager.load();
  }

  public boolean hasLevel(int level) {
    return levelInitLoader.hasLevel(level);
  }

  public void changeLevel(int level, World world, WorldState worldState) {
    Optional<LevelProgress> levelProgressOptional = levelProgressManager.load();
    if (!levelProgressOptional.isPresent()) {
      throw new IllegalStateException(
          "An existing level progress file is needed for changing levels.");
    }

    world.fullHealPlayers();

    LevelProgress levelProgress = levelProgressOptional.get();
    levelProgress.update(world, worldState);

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
      LevelInit levelInit = levelInitLoader.load(level);
      levelProgress = new LevelProgress(gameSave, levelInit);
      levelProgressManager.save(levelProgress);
    }

    Map<Coordinate, Terrain> terrainMap = levelTerrainsLoader.load(level, tiledMapRenderer);
    Map<Coordinate, Ship> shipMap = levelProgress.getActiveShips();
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
