package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Coordinate;

interface TargetFactory {

  Target create(
      @Assisted("select") Iterable<Coordinate> selectCoordinates,
      @Assisted("target") Iterable<Coordinate> targetCoordinates);
}
