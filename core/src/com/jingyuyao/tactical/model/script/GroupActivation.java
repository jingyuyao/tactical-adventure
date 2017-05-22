package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;

public class GroupActivation {

  private ShipGroup group;
  private List<Coordinate> spawns;

  private GroupActivation() {
  }

  public ShipGroup getGroup() {
    return group;
  }

  public List<Coordinate> getSpawns() {
    return spawns;
  }
}
