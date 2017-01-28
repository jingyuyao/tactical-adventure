package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;

abstract class AbstractTerrain extends AbstractMapObject<MapObjectData> implements Terrain {

  AbstractTerrain(MapObjectData data, Multiset<Marker> markers) {
    super(data, markers);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
