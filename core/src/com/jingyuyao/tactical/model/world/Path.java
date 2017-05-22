package com.jingyuyao.tactical.model.world;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * A path is a list of one or more cells.
 */
public class Path {

  private final ImmutableList<Cell> track;

  Path(ImmutableList<Cell> track) {
    Preconditions.checkArgument(!track.isEmpty());
    this.track = track;
  }

  /**
   * Return the cells of this path in sequence. First cell is the origin and the last cell is the
   * destination. Origin and destination may be the same.
   */
  public ImmutableList<Cell> getTrack() {
    return track;
  }

  Cell getOrigin() {
    return track.get(0);
  }

  Cell getDestination() {
    return track.get(track.size() - 1);
  }
}
