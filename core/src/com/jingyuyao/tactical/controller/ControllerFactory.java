package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.world.Cell;

public interface ControllerFactory {

  CellController create(Cell cell);
}
