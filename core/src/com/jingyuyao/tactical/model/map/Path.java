package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Path {

  private final Coordinate destination;
  private final ImmutableList<Coordinate> track;

  Path(Coordinate destination, ImmutableList<Coordinate> track) {
    Preconditions.checkArgument(!track.isEmpty());
    this.destination = destination;
    this.track = track;
  }

  public ImmutableList<Coordinate> getTrack() {
    return track;
  }

  public Coordinate getDestination() {
    return destination;
  }
}
