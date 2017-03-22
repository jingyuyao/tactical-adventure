package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.Map;

public class WorldLoad extends ObjectEvent<Map<Coordinate, Cell>> {

  public WorldLoad(Map<Coordinate, Cell> object) {
    super(object);
  }
}
