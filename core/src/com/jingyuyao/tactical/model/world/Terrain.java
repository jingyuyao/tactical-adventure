package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Terrain {

  private String name;
  private boolean holdShip;
  private int moveCost;

  private Terrain() {
  }

  public Terrain(String name, boolean holdShip, int moveCost) {
    this.name = name;
    this.holdShip = holdShip;
    this.moveCost = moveCost;
  }

  public ResourceKey getName() {
    return ModelBundle.TERRAIN_NAME.get(name);
  }

  public boolean canHoldShip() {
    return holdShip;
  }

  public int getMoveCost() {
    return moveCost;
  }
}
