package com.jingyuyao.tactical.model.map;

import com.google.common.graph.Graph;

interface MovementFactory {

  Movement create(Graph<Coordinate> moveGraph);
}
