package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.map.Coordinate;

public interface TerrainFactory {

  Land createLand(Coordinate coordinate);

  Obstructed createObstructed(Coordinate coordinate);

  Water createWater(Coordinate coordinate);
}
