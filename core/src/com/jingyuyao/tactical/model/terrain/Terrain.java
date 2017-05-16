package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;

public interface Terrain {

  ResourceKey getName();

  boolean canHold(Ship ship);

  int getMovementPenalty();
}
