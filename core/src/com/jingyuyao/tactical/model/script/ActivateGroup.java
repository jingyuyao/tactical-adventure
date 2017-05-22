package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.List;

public class ActivateGroup {

  private ShipGroup group;
  private List<Coordinate> spawns;

  private ActivateGroup() {
  }

  public ShipGroup getGroup() {
    return group;
  }

  public List<Coordinate> getSpawns() {
    return spawns;
  }
}
