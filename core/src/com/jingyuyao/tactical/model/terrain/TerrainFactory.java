package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain.Type;

public interface TerrainFactory {

  Terrain create(Coordinate coordinate, Type type);
}
