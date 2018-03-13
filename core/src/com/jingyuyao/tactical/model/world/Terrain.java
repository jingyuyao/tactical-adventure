package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.Identifiable;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import java.util.List;

public class Terrain extends Identifiable {

  private String name;
  private List<IntKey> textures;
  private boolean holdShip;
  private int moveCost;

  Terrain() {
  }

  public Terrain(String name, List<IntKey> textures, boolean holdShip, int moveCost) {
    this.name = name;
    this.textures = textures;
    this.holdShip = holdShip;
    this.moveCost = moveCost;
  }

  public StringKey getName() {
    return ModelBundle.TERRAIN_NAME.get(name);
  }

  public IntKey getTexture() {
    return textures.get(0);
  }

  public List<IntKey> getTextures() {
    return textures;
  }

  public boolean canHoldShip() {
    return holdShip;
  }

  public int getMoveCost() {
    return moveCost;
  }
}
