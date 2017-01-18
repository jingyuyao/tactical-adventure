package com.jingyuyao.tactical.model.target;

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;

public interface TargetFactory {

  Target create(
      @Assisted("select") ImmutableSet<Coordinate> selectCoordinates,
      @Assisted("target") ImmutableSet<Coordinate> targetCoordinates);
}
