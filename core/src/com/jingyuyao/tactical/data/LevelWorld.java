package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelWorld {

  private List<Coordinate> playerSpawns = new ArrayList<>();
  private Map<Coordinate, Ship> activeShips = new HashMap<>();
  private List<Ship> inactiveShips = new ArrayList<>();

  List<Coordinate> getPlayerSpawns() {
    return playerSpawns;
  }

  Map<Coordinate, Ship> getActiveShips() {
    return activeShips;
  }

  public List<Ship> getInactiveShips() {
    return inactiveShips;
  }
}
