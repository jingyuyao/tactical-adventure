package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain.Type;

public interface TerrainFactory {

  Terrain create(Coordinate coordinate, Type type);
}
