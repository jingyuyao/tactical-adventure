package com.jingyuyao.tactical.model.world;

class OneCost implements GetEdgeCost {

  @Override
  public int getEdgeCost(Cell cell) {
    return 1;
  }
}
