package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.AbstractMapObject;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import javax.inject.Inject;

public class Terrain extends AbstractMapObject {

  private final Type type;

  @Inject
  Terrain(
      @Assisted Coordinate coordinate,
      @InitialMarkers Multiset<Marker> markers,
      @Assisted Type type) {
    super(coordinate, markers);
    this.type = type;
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  public Type getType() {
    return type;
  }

  public int getMovementPenalty() {
    switch (type) {
      case OBSTRUCTED:
        return 2;
      case WATER:
      case MOUNTAIN:
        return 3;
      case NORMAL:
      default:
        return 1;
    }
  }

  public enum Type {
    NORMAL,
    OBSTRUCTED,
    WATER,
    MOUNTAIN
  }
}
