package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.world.Coordinate;

class CoordinatePanel extends TextPanel<Coordinate> {

  @Override
  String createText(Coordinate coordinate) {
    return coordinate.toString();
  }
}
