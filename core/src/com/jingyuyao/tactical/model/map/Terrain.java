package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Algorithms;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;

public class Terrain extends MapObject {

  private final Type type;

  Terrain(EventBus eventBus, Coordinate coordinate, List<Marker> markers, Type type) {
    super(eventBus, coordinate, markers);
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

  public int getMovementPenalty(Character character) {
    if (!character.canPassTerrainType(type)) {
      return Algorithms.NO_EDGE;
    }

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
