package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Path {

  private final Cell destination;
  private final ImmutableList<Cell> track;

  Path(Cell destination, ImmutableList<Cell> track) {
    Preconditions.checkArgument(!track.isEmpty());
    this.destination = destination;
    this.track = track;
  }

  public ImmutableList<Cell> getTrack() {
    return track;
  }

  public Cell getDestination() {
    return destination;
  }
}
