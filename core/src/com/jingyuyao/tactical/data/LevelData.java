package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelData {

  private List<Coordinate> playerSpawns = new ArrayList<>();
  private Map<Coordinate, Enemy> enemies = new HashMap<>();

  public List<Coordinate> getPlayerSpawns() {
    return playerSpawns;
  }

  public Map<Coordinate, Enemy> getEnemies() {
    return enemies;
  }
}
