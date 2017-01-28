package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.map.MapObjectData;

public interface TerrainFactory {

  Land createLand(MapObjectData mapObjectData);

  Obstructed createObstructed(MapObjectData mapObjectData);

  Water createWater(MapObjectData data);
}
