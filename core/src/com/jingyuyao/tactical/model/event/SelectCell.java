package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.world.Cell;

public class SelectCell extends ObjectEvent<Cell> {

  public SelectCell(Cell object) {
    super(object);
  }
}
