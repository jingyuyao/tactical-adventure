package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;
import java.util.Map;

public class LevelData {

  private List<Coordinate> playerSpawns;
  private Map<Coordinate, Enemy> enemies;
}
