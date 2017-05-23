package com.jingyuyao.tactical.model.world;

class TerrainCost implements GetEdgeCost {

  @Override
  public int getEdgeCost(Cell cell) {
    Terrain terrain = cell.getTerrain();
    if (cell.ship().isPresent() || !terrain.canHoldShip()) {
      return GetEdgeCost.NO_EDGE;
    }
    return terrain.getMoveCost();
  }
}
