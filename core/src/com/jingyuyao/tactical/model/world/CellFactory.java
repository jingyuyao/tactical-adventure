package com.jingyuyao.tactical.model.world;

import com.jingyuyao.tactical.model.terrain.Terrain;

public interface CellFactory {

  Cell create(Coordinate coordinate, Terrain terrain);
}
