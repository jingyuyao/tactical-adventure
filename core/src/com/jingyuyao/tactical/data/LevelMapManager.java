package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.terrain.Land;
import com.jingyuyao.tactical.model.terrain.Obstructed;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.terrain.Water;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
@Singleton
class LevelMapManager {

  private final DataConfig dataConfig;
  private final AssetManager assetManager;

  @Inject
  LevelMapManager(DataConfig dataConfig, AssetManager assetManager) {
    this.dataConfig = dataConfig;
    this.assetManager = assetManager;
  }

  Map<Coordinate, Terrain> load(int level, OrthogonalTiledMapRenderer tiledMapRenderer) {
    String levelFileName = dataConfig.getLevelMapFileName(level);
    assetManager.load(levelFileName, TiledMap.class);
    assetManager.finishLoadingAsset(levelFileName);
    TiledMap tiledMap = assetManager.get(levelFileName);
    tiledMapRenderer.setMap(tiledMap);
    return extractTerrains(tiledMap);
  }

  private Map<Coordinate, Terrain> extractTerrains(TiledMap tiledMap) {
    TiledMapTileLayer terrainLayer =
        (TiledMapTileLayer) tiledMap.getLayers().get(dataConfig.getTerrainLayerKey());
    Preconditions.checkNotNull(terrainLayer);

    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    for (int y = 0; y < terrainLayer.getHeight(); y++) {
      for (int x = 0; x < terrainLayer.getWidth(); x++) {
        TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
        terrainMap.put(new Coordinate(x, y), createTerrain(cell));
      }
    }
    return terrainMap;
  }

  private Terrain createTerrain(TiledMapTileLayer.Cell cell) {
    if (cell != null) {
      MapProperties tileProperties = cell.getTile().getProperties();
      String type = tileProperties.get(dataConfig.getTerrainTypeKey(), String.class);
      if (type != null) {
        switch (type) {
          case "OBSTRUCTED":
            return new Obstructed();
          case "WATER":
            return new Water();
          default:
            throw new IllegalArgumentException("Unrecognized terrain type: " + type);
        }
      }
    }
    return new Land();
  }
}
