package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.Cell;

public class SpawnCharacter extends ObjectEvent<Cell> {

  public SpawnCharacter(Cell object) {
    super(object);
  }
}
