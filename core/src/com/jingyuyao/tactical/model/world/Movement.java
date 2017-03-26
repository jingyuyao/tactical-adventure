package com.jingyuyao.tactical.model.world;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.Graph;
import java.util.Set;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Movement {

  private final Graph<Cell> moveGraph;

  Movement(Graph<Cell> moveGraph) {
    this.moveGraph = moveGraph;
  }

  public Cell getStartingCell() {
    return moveGraph.nodes().iterator().next();
  }

  public Iterable<Cell> getCells() {
    return moveGraph.nodes();
  }

  /**
   * Can move to {@code coordinate}.
   */
  public boolean canMoveTo(Cell cell) {
    return moveGraph.nodes().contains(cell);
  }

  /**
   * Get a path to {@code coordinate}.
   */
  public Path pathTo(Cell cell) {
    Preconditions.checkArgument(moveGraph.nodes().contains(cell));

    return new Path(getTrackTo(moveGraph, cell));
  }

  /**
   * Get the track from the starting {@link Cell} in the {@code graph} to {@code target}.
   * {@code target} must exist in the {@code graph} or an exception will be thrown.
   *
   * @param graph An directed acyclic graph
   * @param target The target node to find a path to
   * @return A path to {@code target} from the first node in the graph or an empty list if target is
   * not in the graph
   */
  private ImmutableList<Cell> getTrackTo(Graph<Cell> graph, Cell target) {
    Preconditions.checkArgument(graph.nodes().contains(target));

    ImmutableList.Builder<Cell> builder = ImmutableList.builder();
    builder.add(target);

    Set<Cell> predecessors = graph.predecessors(target);
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
