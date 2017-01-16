package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.common.Coordinate;

public class Target {

  private final Coordinate select;
  private final ImmutableSet<Coordinate> targets;

  Target(Coordinate select, ImmutableSet<Coordinate> targets) {
    this.select = select;
    this.targets = targets;
  }

  public Coordinate getSelect() {
    return select;
  }

  public ImmutableSet<Coordinate> getTargets() {
    return targets;
  }
}
