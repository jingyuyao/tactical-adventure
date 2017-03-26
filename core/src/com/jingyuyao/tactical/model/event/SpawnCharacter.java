package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.world.Cell;

public class SpawnCharacter extends ObjectEvent<Cell> {

  public SpawnCharacter(Cell object) {
    super(object);
  }
}
