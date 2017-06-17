package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.Identifiable;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

public class Terrain extends Identifiable {

  private String name;
  private IntKey texture;
  private boolean holdShip;
  private int moveCost;

  Terrain() {
  }

  public Terrain(String name, IntKey texture, boolean holdShip, int moveCost) {
    this.name = name;
    this.texture = texture;
    this.holdShip = holdShip;
    this.moveCost = moveCost;
  }

  public StringKey getName() {
    return ModelBundle.TERRAIN_NAME.get(name);
  }

  public IntKey getTexture() {
    return texture;
  }

  public boolean canHoldShip() {
    return holdShip;
  }

  public int getMoveCost() {
    return moveCost;
  }
}
