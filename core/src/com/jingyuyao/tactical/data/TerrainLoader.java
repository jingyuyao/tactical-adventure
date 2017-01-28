package com.jingyuyao.tactical.data;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.terrain.TerrainFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TerrainLoader {

  private static final String TERRAIN_TYPE_KEY = "type";

  private final TerrainFactory terrainFactory;

  @Inject
  TerrainLoader(TerrainFactory terrainFactory) {
    this.terrainFactory = terrainFactory;
  }

  Iterable<Terrain> createTerrains(TiledMapTileLayer terrainLayer, int width, int height) {
    List<Terrain> terrains = new ArrayList<Terrain>(width * height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
        terrains.add(createTerrain(x, y, cell));
      }
    }
    return terrains;
  }

  private Terrain createTerrain(int x, int y, TiledMapTileLayer.Cell cell) {
    MapObjectData data = new MapObjectData(new Coordinate(x, y));

    MapProperties tileProperties = cell.getTile().getProperties();
    if (tileProperties.containsKey(TERRAIN_TYPE_KEY)) {
      String type = tileProperties.get(TERRAIN_TYPE_KEY, String.class);
      if (type.equals("OBSTRUCTED")) {
        return terrainFactory.createObstructed(data);
      } else if (type.equals("WATER")) {
        return terrainFactory.createWater(data);
      } else {
        throw new IllegalArgumentException("Unrecognized terrain type: " + type);
      }
    }
    return terrainFactory.createLand(data);
  }
}
