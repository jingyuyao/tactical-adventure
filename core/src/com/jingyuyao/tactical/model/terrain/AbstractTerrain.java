package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;

abstract class AbstractTerrain extends AbstractMapObject implements Terrain {

  AbstractTerrain(Coordinate coordinate, Multiset<Marker> markers) {
    super(coordinate, markers);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }
}
