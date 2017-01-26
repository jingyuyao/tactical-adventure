package com.jingyuyao.tactical.model.map;

import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.common.Coordinate;

public interface MovementFactory {

  Movement create(Graph<Coordinate> moveGraph);
}
