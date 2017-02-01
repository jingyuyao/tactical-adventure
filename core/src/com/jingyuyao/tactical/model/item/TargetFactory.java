package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.Set;

interface TargetFactory {

  Target create(
      @Assisted("select") Set<Coordinate> selectCoordinates,
      @Assisted("target") Set<Coordinate> targetCoordinates);
}
