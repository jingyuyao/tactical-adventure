package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.terrain.Terrain;

public interface CellFactory {

  Cell create(Coordinate coordinate, Terrain terrain);
}
