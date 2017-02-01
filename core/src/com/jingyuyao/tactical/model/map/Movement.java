package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.Graph;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Marking.MarkingBuilder;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Set;
import javax.inject.Inject;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Movement {

  private final Graph<Coordinate> moveGraph;
  private final Terrains terrains;
  private Marking marking;

  @Inject
  Movement(Terrains terrains, @Assisted Graph<Coordinate> moveGraph) {
    this.terrains = terrains;
    this.moveGraph = moveGraph;
  }

  /**
   * Can move to {@code coordinate}.
   */
  public boolean canMoveTo(Coordinate coordinate) {
    return moveGraph.nodes().contains(coordinate);
  }

  /**
   * Get the {@link Terrain}s this target can move to.
   */
  public Iterable<Coordinate> getCoordinates() {
    return moveGraph.nodes();
  }

  public Iterable<Terrain> getTerrains() {
    return terrains.getAll(moveGraph.nodes());
  }

  /**
   * Get a path to {@code coordinate}.
   */
  public Path pathTo(Coordinate coordinate) {
    Preconditions.checkArgument(moveGraph.nodes().contains(coordinate));

    return new Path(coordinate, getTrackTo(moveGraph, coordinate));
  }

  public void showMarking() {
    marking = createMarking();
    marking.apply();
  }

  public void hideMarking() {
    marking.clear();
    marking = null;
  }

  private Marking createMarking() {
    MarkingBuilder builder = new MarkingBuilder();
    for (Terrain terrain : terrains.getAll(moveGraph.nodes())) {
      builder.put(terrain, Marker.CAN_MOVE_TO);
    }
    return builder.build();
  }

  /**
   * Get the track from the starting {@link Coordinate} in the {@code graph} to {@code target}.
   * {@code target} must exist in the {@code graph} or an exception will be thrown.
   *
   * @param graph An directed acyclic graph
   * @param target The target node to find a path to
   * @return A path to {@code target} from the first node in the graph or an empty list if target is
   * not in the graph
   */
  private ImmutableList<Coordinate> getTrackTo(Graph<Coordinate> graph, Coordinate target) {
    Preconditions.checkArgument(graph.nodes().contains(target));

    ImmutableList.Builder<Coordinate> builder = new ImmutableList.Builder<Coordinate>();
    builder.add(target);

    Set<Coordinate> predecessors = graph.predecessors(target);
    while (predecessors.size() != 0) {
      Preconditions.checkState(
          predecessors.size() == 1, "getTrackTo encountered a node with multiple predecessors");
      Coordinate predecessor = predecessors.iterator().next();
      builder.add(predecessor);
      predecessors = graph.predecessors(predecessor);
    }

    return builder.build().reverse();
  }
}
