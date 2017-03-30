package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelData {

  private int id;
  private List<Coordinate> playerSpawns = new ArrayList<>();
  private Map<Coordinate, Enemy> enemies = new HashMap<>();

  int getId() {
    return id;
  }

  List<Coordinate> getPlayerSpawns() {
    return playerSpawns;
  }

  Map<Coordinate, Enemy> getEnemies() {
    return enemies;
  }
}
