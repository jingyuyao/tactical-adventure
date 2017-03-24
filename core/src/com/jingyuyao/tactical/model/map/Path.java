package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Path {

  private final ImmutableList<Cell> track;

  Path(ImmutableList<Cell> track) {
    Preconditions.checkArgument(!track.isEmpty());
    this.track = track;
  }

  public ImmutableList<Cell> getTrack() {
    return track;
  }

  public Cell getOrigin() {
    return track.get(0);
  }

  public Cell getDestination() {
    return track.get(track.size() - 1);
  }
}
