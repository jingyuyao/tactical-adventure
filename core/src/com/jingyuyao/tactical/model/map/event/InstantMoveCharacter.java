package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.Cell;

public class InstantMoveCharacter extends ObjectEvent<Cell> {

  public InstantMoveCharacter(Cell destination) {
    super(destination);
  }
}
