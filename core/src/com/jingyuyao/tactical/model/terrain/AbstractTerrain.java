package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Coordinate;

abstract class AbstractTerrain extends AbstractMapObject implements Terrain {

  AbstractTerrain(Coordinate coordinate) {
    super(coordinate);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
