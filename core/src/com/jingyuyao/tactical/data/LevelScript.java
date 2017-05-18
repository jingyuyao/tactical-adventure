package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.List;

class LevelScript {

  private List<Coordinate> playerSpawns = new ArrayList<>();

  public List<Coordinate> getPlayerSpawns() {
    return playerSpawns;
  }
}
