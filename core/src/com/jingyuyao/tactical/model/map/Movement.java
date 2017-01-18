package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import java.util.HashMap;
import java.util.Map;

/**
 * A snapshot of all the things a character currently can target or move to on the map.
 */
public class Movement {

  private final Algorithms algorithms;
  private final Terrains terrains;
  private final Graph<Coordinate> moveGraph;
  private Marking marking;

  Movement(Algorithms algorithms, Terrains terrains, Graph<Coordinate> moveGraph) {
    this.algorithms = algorithms;
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
  public Iterable<Terrain> getTerrains() {
    return terrains.getAll(moveGraph.nodes());
  }

  /**
   * Get a path to {@code coordinate}.
   */
  public Path pathTo(Coordinate coordinate) {
    Preconditions.checkArgument(moveGraph.nodes().contains(coordinate));

    return new Path(coordinate, algorithms.getTrackTo(moveGraph, coordinate));
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
    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : terrains.getAll(moveGraph.nodes())) {
      markerMap.put(terrain, Marker.CAN_MOVE_TO);
    }
    return new Marking(markerMap);
  }
}
