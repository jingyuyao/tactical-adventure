package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;

public class Path {
  private final ImmutableList<Coordinate> track;
  private final Coordinate destination;

  Path(ImmutableList<Coordinate> track) {
    Preconditions.checkArgument(!track.isEmpty());
    this.track = track;
    this.destination = track.get(track.size() - 1);
  }

  public ImmutableList<Coordinate> getTrack() {
    return track;
  }

  public Coordinate getDestination() {
    return destination;
  }
}
