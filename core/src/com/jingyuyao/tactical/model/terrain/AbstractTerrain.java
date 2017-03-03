package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.state.SelectionHandler;

abstract class AbstractTerrain extends AbstractMapObject implements Terrain {

  AbstractTerrain(Coordinate coordinate) {
    super(coordinate);
  }

  @Override
  public void select(SelectionHandler selectionHandler) {
    selectionHandler.select(this);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
