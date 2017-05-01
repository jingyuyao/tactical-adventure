package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.world.Coordinate;
import javax.inject.Singleton;

@Singleton
class CoordinatePanel extends TextPanel<Coordinate> {

  CoordinatePanel() {
    super(Align.right);
  }

  @Override
  String createText(Coordinate coordinate) {
    return coordinate.toString();
  }
}
