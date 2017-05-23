package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Blocked implements Terrain {

  @Override
  public ResourceKey getName() {
    return ModelBundle.TERRAIN_NAME.get("blocked");
  }

  @Override
  public boolean canHoldShip() {
    return false;
  }

  @Override
  public int getMovementPenalty() {
    return 0;
  }
}
