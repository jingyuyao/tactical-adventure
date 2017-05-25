package com.jingyuyao.tactical.model.world;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.Graph;
import java.util.Set;

/**
 * A movement that contains many {@link Path} from an origin {@link Cell}. Note: a path from the
 * origin to the origin is also valid.
 */
public class Movement {

  private final Graph<Cell> moveGraph;

  /**
   * @param moveGraph an acyclic graph that represents movement where the first node is the root
   * node.
   */
  Movement(Graph<Cell> moveGraph) {
    this.moveGraph = moveGraph;
  }

  /**
   * Return the origin cell of this movement.
   */
  public Cell getOrigin() {
    return moveGraph.nodes().iterator().next();
  }

  /**
   * Return the cells that are part of this movement.
   */
  public Set<Cell> getCells() {
    return moveGraph.nodes();
  }

  /**
   * Return whether or not {@code dest} is part of this movement.
   */
  public boolean canMoveTo(Cell dest) {
    return moveGraph.nodes().contains(dest);
  }

  /**
   * Get a path from {@link #getOrigin()} to {@code dest}.
   */
  public Path pathTo(Cell dest) {
    Preconditions.checkArgument(canMoveTo(dest));
    return new Path(getTrackTo(moveGraph, dest));
  }

  Graph<Cell> getMoveGraph() {
    return moveGraph;
  }

  /**
   * Get the track from the starting {@link Cell} in the {@code graph} to {@code dest}.
   * {@code dest} must exist in the {@code graph} or an exception will be thrown.
   *
   * @param graph An directed acyclic graph
   * @param dest The dest node to find a path to
   * @return A path to {@code dest} from the first node in the graph or an empty list if dest is not
   * in the graph
   */
  private ImmutableList<Cell> getTrackTo(Graph<Cell> graph, Cell dest) {
    ImmutableList.Builder<Cell> builder = ImmutableList.builder();
    builder.add(dest);

    Set<Cell> predecessors = graph.predecessors(dest);
    while (predecessors.size() != 0) {
      Preconditions.checkState(
          predecessors.size() == 1, "getTrackTo encountered a node with multiple predecessors");
      Cell predecessor = predecessors.iterator().next();
      builder.add(predecessor);
      predecessors = graph.predecessors(predecessor);
    }

    return builder.build().reverse();
  }
}
