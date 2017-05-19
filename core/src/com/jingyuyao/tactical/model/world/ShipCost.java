package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.terrain.Terrain;

class ShipCost implements GetEdgeCost {

  private final Ship walker;

  ShipCost(Ship walker) {
    this.walker = walker;
  }

  @Override
  public int getEdgeCost(Cell cell) {
    Terrain terrain = cell.getTerrain();
    if (cell.ship().isPresent() || !terrain.canHold(walker)) {
      return GetEdgeCost.NO_EDGE;
    }
    return terrain.getMovementPenalty();
  }
}
