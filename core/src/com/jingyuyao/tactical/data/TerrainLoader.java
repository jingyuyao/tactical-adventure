package com.jingyuyao.tactical.data;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.terrain.TerrainFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainLoader {

  private final DataConfig dataConfig;
  private final TerrainFactory terrainFactory;

  @Inject
  TerrainLoader(DataConfig dataConfig, TerrainFactory terrainFactory) {
    this.dataConfig = dataConfig;
    this.terrainFactory = terrainFactory;
  }

  Iterable<Terrain> createTerrains(TiledMapTileLayer terrainLayer, int width, int height) {
    List<Terrain> terrains = new ArrayList<>(width * height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
        terrains.add(createTerrain(x, y, cell));
      }
    }
    return terrains;
  }

  private Terrain createTerrain(int x, int y, TiledMapTileLayer.Cell cell) {
    Coordinate coordinate = new Coordinate(x, y);

    MapProperties tileProperties = cell.getTile().getProperties();
    if (tileProperties.containsKey(dataConfig.getTerrainTypeKey())) {
      String type = tileProperties.get(dataConfig.getTerrainTypeKey(), String.class);
      switch (type) {
        case "OBSTRUCTED":
          return terrainFactory.createObstructed(coordinate);
        case "WATER":
          return terrainFactory.createWater(coordinate);
        default:
          throw new IllegalArgumentException("Unrecognized terrain type: " + type);
      }
    }
    return terrainFactory.createLand(coordinate);
  }
}
