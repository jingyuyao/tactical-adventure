package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.resource.ResourceKey;

public interface Terrain {

  ResourceKey getName();

  boolean canHoldShip();

  int getMovementPenalty();
}
