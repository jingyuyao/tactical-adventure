package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.world.Coordinate;
import javax.inject.Singleton;

@Singleton
class CoordinatePanel extends TextPanel<Coordinate> {

  @Override
  String createText(Coordinate coordinate) {
    return coordinate.toString();
  }
}
