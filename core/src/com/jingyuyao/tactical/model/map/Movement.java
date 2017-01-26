package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Marking.MarkingBuilder;
import javax.inject.Inject;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Movement {

  private final Graph<Coordinate> moveGraph;
  private final TerrainGraphs terrainGraphs;
  private final Terrains terrains;
  private Marking marking;

  @Inject
  Movement(TerrainGraphs terrainGraphs, Terrains terrains, @Assisted Graph<Coordinate> moveGraph) {
    this.terrainGraphs = terrainGraphs;
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

  /**
   * Get a path to {@code coordinate}.
   */
  public Path pathTo(Coordinate coordinate) {
    Preconditions.checkArgument(moveGraph.nodes().contains(coordinate));

    return new Path(coordinate, terrainGraphs.getTrackTo(moveGraph, coordinate));
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
}
