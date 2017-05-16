package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.world.Cell;

public class SpawnShip extends ObjectEvent<Cell> {

  public SpawnShip(Cell object) {
    super(object);
  }
}
