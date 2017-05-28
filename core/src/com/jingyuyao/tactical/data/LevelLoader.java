package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Terrain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LevelLoader {

  private final DataConfig dataConfig;
  private final InitLoader initLoader;
  private final Files files;
  private final TerrainsLoader terrainsLoader;
  private final ScriptLoader scriptLoader;

  @Inject
  LevelLoader(
      DataConfig dataConfig,
      InitLoader initLoader,
      Files files,
      TerrainsLoader terrainsLoader,
      ScriptLoader scriptLoader) {
    this.dataConfig = dataConfig;
    this.initLoader = initLoader;
    this.files = files;
    this.terrainsLoader = terrainsLoader;
    this.scriptLoader = scriptLoader;
  }

  boolean hasLevel(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelDir(level));
    return fileHandle.exists();
  }

  /**
   * Create a new level save.
   */
  LevelSave createNewSave(int level, List<Ship> playerShips) {
    Preconditions.checkArgument(hasLevel(level));
    FileHandle fileHandle = files.internal(dataConfig.getLevelWorldFileName(level));
    LevelWorld levelWorld = initLoader.fromHocon(fileHandle.reader(), LevelWorld.class);
    Map<Coordinate, Ship> worldShips = new HashMap<>(levelWorld.getActiveShips());
    List<Coordinate> spawns = levelWorld.getPlayerSpawns();
    for (int i = 0; i < spawns.size() && i < playerShips.size(); i++) {
      worldShips.put(spawns.get(i), playerShips.get(i));
    }
    List<Cell> worldCells = new ArrayList<>();
    for (Entry<Coordinate, Terrain> entry : terrainsLoader.load(level).entrySet()) {
      Coordinate coordinate = entry.getKey();
      Terrain terrain = entry.getValue();
      if (terrain.canHoldShip() && worldShips.containsKey(coordinate)) {
        worldCells.add(new Cell(coordinate, terrain, worldShips.get(coordinate)));
      } else {
        worldCells.add(new Cell(coordinate, terrain));
      }
    }
    List<Ship> inactiveShips = new ArrayList<>(levelWorld.getInactiveShips());
    for (int i = spawns.size(); i < playerShips.size(); i++) {
      inactiveShips.add(playerShips.get(i));
    }
    return new LevelSave(level, worldCells, inactiveShips, new Turn(), scriptLoader.load(level));
  }
}
