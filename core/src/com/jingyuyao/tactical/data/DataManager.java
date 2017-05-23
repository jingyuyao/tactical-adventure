package com.jingyuyao.tactical.data;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Public api of all game data related functions.
 */
@Singleton
public class DataManager {

  private final SaveManager saveManager;
  private final LevelLoader levelLoader;

  @Inject
  DataManager(SaveManager saveManager, LevelLoader levelLoader) {
    this.saveManager = saveManager;
    this.levelLoader = levelLoader;
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
    int level = world.getLevel();
    List<Cell> worldCells = world.getWorldCells();
    List<Ship> inactiveShips = world.getInactiveShips();
    Turn turn = worldState.getTurn();
    Script script = worldState.getScript();
    LevelSave levelSave = new LevelSave(level, worldCells, inactiveShips, turn, script);
    saveManager.save(levelSave);
  }

  public void removeLevelProgress() {
    saveManager.removeLevelSave();
  }

  public void changeLevel(int level, World world) {
    world.makeAllPlayerShipsControllable();

    GameSave gameSave = saveManager.loadGameSave();
    gameSave.setCurrentLevel(level);
    gameSave.replacePlayerShipsFrom(world);

    saveManager.save(gameSave);
    saveManager.removeLevelSave();
  }

  public LevelSave loadCurrentLevel() {
    Optional<LevelSave> levelSaveOpt = saveManager.loadLevelSave();
    if (levelSaveOpt.isPresent()) {
      return levelSaveOpt.get();
    } else {
      GameSave gameSave = saveManager.loadGameSave();
      int level = gameSave.getCurrentLevel();
      LevelSave levelSave = levelLoader.createNewSave(level, gameSave.getPlayerShips());
      saveManager.save(levelSave);
      return levelSave;
    }
  }
}
