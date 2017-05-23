package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;
import java.util.Map;

public class LoadedLevel {

  private final int level;
  private final Map<Coordinate, Terrain> terrainMap;
  private final Map<Coordinate, Ship> activeShips;
  private final List<Ship> inactiveShips;
  private final Turn turn;
  private final Script script;

  LoadedLevel(
      int level, Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Ship> activeShips,
      List<Ship> inactiveShips,
      Turn turn,
      Script script) {
    this.level = level;
    this.terrainMap = terrainMap;
    this.activeShips = activeShips;
    this.inactiveShips = inactiveShips;
    this.turn = turn;
    this.script = script;
  }

  public int getLevel() {
    return level;
  }

  public Map<Coordinate, Terrain> getTerrainMap() {
    return terrainMap;
  }

  public Map<Coordinate, Ship> getActiveShips() {
    return activeShips;
  }

  public List<Ship> getInactiveShips() {
    return inactiveShips;
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }
}
