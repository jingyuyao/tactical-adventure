package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelData {

  private List<Coordinate> playerSpawns = new ArrayList<>();
  private Map<Coordinate, Ship> enemies = new HashMap<>();

  List<Coordinate> getPlayerSpawns() {
    return playerSpawns;
  }

  Map<Coordinate, Ship> getEnemies() {
    return enemies;
  }
}
