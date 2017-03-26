package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.world.Coordinate;

public interface TerrainFactory {

  Land createLand(Coordinate coordinate);

  Obstructed createObstructed(Coordinate coordinate);

  Water createWater(Coordinate coordinate);
}
