package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

public class Terrain {

  private final String name;
  private final boolean holdShip;
  private final int moveCost;

  public Terrain(String name, boolean holdShip, int moveCost) {
    this.name = name;
    this.holdShip = holdShip;
    this.moveCost = moveCost;
  }

  public StringKey getName() {
    return ModelBundle.TERRAIN_NAME.get(name);
  }

  public boolean canHoldShip() {
    return holdShip;
  }

  public int getMoveCost() {
    return moveCost;
  }
}
