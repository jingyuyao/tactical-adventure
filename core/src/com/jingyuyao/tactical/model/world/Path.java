package com.jingyuyao.tactical.model.world;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Path to move in the world grid.
 */
public class Path {

  private final ImmutableList<Cell> track;

  /**
   * Creates a path where the first cell is the starting point and the last cell is the destination.
   */
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
