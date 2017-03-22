package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Cell;

public class WorldLoad extends ObjectEvent<Iterable<Cell>> {

  public WorldLoad(Iterable<Cell> object) {
    super(object);
  }
}
