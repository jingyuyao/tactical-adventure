package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.world.Coordinate;
import javax.inject.Singleton;

@Singleton
class CoordinatePanel extends TextPanel<Coordinate> {

  CoordinatePanel() {
    super(Align.right);
  }

  @Override
  Optional<String> createText(Coordinate coordinate) {
    return Optional.of(coordinate.toString());
  }
}
