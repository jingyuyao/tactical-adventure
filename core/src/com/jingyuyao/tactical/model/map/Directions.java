package com.jingyuyao.tactical.model.map;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.common.Coordinate;

public class Directions {

  public static final Coordinate UP = new Coordinate(0, 1);
  public static final Coordinate DOWN = new Coordinate(0, -1);
  public static final Coordinate LEFT = new Coordinate(-1, 0);
  public static final Coordinate RIGHT = new Coordinate(1, 0);
  public static final ImmutableList<Coordinate> ALL = ImmutableList.of(UP, DOWN, LEFT, RIGHT);
}
