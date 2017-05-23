package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Obstructed implements Terrain {

  @Override
  public ResourceKey getName() {
    return ModelBundle.TERRAIN_NAME.get("obstructed");
  }

  @Override
  public boolean canHoldShip() {
    return true;
  }

  @Override
  public int getMovementPenalty() {
    return 2;
  }
}
