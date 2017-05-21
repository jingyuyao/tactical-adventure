package com.jingyuyao.tactical.data;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Public api of all game data related functions.
 */
@Singleton
public class DataManager {

  private final SaveManager saveManager;
  private final LevelLoader levelLoader;
  private final LevelTerrainsLoader levelTerrainsLoader;

  @Inject
  DataManager(
      SaveManager saveManager,
      LevelLoader levelLoader,
      LevelTerrainsLoader levelTerrainsLoader) {
    this.saveManager = saveManager;
    this.levelLoader = levelLoader;
    this.levelTerrainsLoader = levelTerrainsLoader;
  }

  public GameSave loadGameSave() {
    return saveManager.loadGameSave();
  }

  public Optional<LevelSave> loadLevelSave() {
    return saveManager.loadLevelSave();
  }

  public boolean hasLevel(int level) {
    return levelLoader.hasLevel(level);
  }

  public void freshStart() {
    saveManager.removeGameSave();
    saveManager.removeLevelSave();
  }

  public void saveLevelProgress(World world, WorldState worldState) {
    Map<Coordinate, Ship> ships = new HashMap<>();
    for (Entry<Cell, Ship> shipEntry : world.getShipSnapshot().entrySet()) {
      ships.put(shipEntry.getKey().getCoordinate(), shipEntry.getValue());
    }
    LevelSave levelSave = new LevelSave(ships, worldState.getTurn(), worldState.getScript());
    saveManager.save(levelSave);
  }

  public void removeLevelProgress() {
    GameSave gameSave = saveManager.loadGameSave();
    gameSave.deactivateShips();
    saveManager.save(gameSave);
    saveManager.removeLevelSave();
  }

  public void changeLevel(int level, World world) {
    world.makeAllPlayerShipsControllable();

    GameSave gameSave = saveManager.loadGameSave();
    gameSave.setCurrentLevel(level);
    gameSave.replaceActiveShipsFrom(world);
    gameSave.deactivateShips();

    saveManager.save(gameSave);
    saveManager.removeLevelSave();
  }

  public LoadedLevel loadCurrentLevel(OrthogonalTiledMapRenderer tiledMapRenderer) {
    GameSave gameSave = saveManager.loadGameSave();
    int level = gameSave.getCurrentLevel();

    LevelSave levelSave;
    Optional<LevelSave> levelSaveOpt = saveManager.loadLevelSave();
    if (levelSaveOpt.isPresent()) {
      levelSave = levelSaveOpt.get();
    } else {
      levelSave = levelLoader.createNewSave(level, gameSave);
      saveManager.save(gameSave);
      saveManager.save(levelSave);
    }

    Map<Coordinate, Terrain> terrains = levelTerrainsLoader.load(level, tiledMapRenderer);
    Map<Coordinate, Ship> ships = levelSave.getShips();
    Turn turn = levelSave.getTurn();
    Script script = levelSave.getScript();
    return new LoadedLevel(terrains, ships, turn, script);
  }
}
