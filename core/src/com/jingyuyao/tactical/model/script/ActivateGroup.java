package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.io.Serializable;
import java.util.List;

public class ActivateGroup implements Serializable {

  private ShipGroup group;
  private List<Coordinate> spawns;

  ActivateGroup() {
  }

  public ShipGroup getGroup() {
    return group;
  }

  public List<Coordinate> getSpawns() {
    return spawns;
  }
}
