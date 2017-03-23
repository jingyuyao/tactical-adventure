package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.map.Cell;

public interface ControllerFactory {

  CellController create(Cell cell);
}
