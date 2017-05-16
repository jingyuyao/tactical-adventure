package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.Map;

public class LoadedLevel {

  private final Map<Coordinate, Terrain> terrainMap;
  private final Map<Coordinate, Ship> shipMap;
  private final Turn turn;
  private final Script script;

  LoadedLevel(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Ship> shipMap,
      Turn turn,
      Script script) {
    this.terrainMap = terrainMap;
    this.shipMap = shipMap;
    this.turn = turn;
    this.script = script;
  }

  public Map<Coordinate, Terrain> getTerrainMap() {
    return terrainMap;
  }

  public Map<Coordinate, Ship> getShipMap() {
    return shipMap;
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }
}
