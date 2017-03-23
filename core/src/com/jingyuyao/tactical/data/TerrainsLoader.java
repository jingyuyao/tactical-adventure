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
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainsLoader {

  private final DataConfig dataConfig;
  private final AssetManager assetManager;
  private final OrthogonalTiledMapRenderer tiledMapRenderer;

  @Inject
  TerrainsLoader(
      DataConfig dataConfig,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer tiledMapRenderer) {
    this.dataConfig = dataConfig;
    this.assetManager = assetManager;
    this.tiledMapRenderer = tiledMapRenderer;
  }

  Iterable<Terrain> loadTerrains(String mapName) {
    String fileName = dataConfig.getTerrainsFileName(mapName);
    assetManager.load(fileName, TiledMap.class);
    assetManager.finishLoadingAsset(fileName);
    TiledMap tiledMap = assetManager.get(fileName);
    tiledMapRenderer.setMap(tiledMap);

    TiledMapTileLayer terrainLayer =
        (TiledMapTileLayer) tiledMap.getLayers().get(dataConfig.getTerrainLayerKey());
    return createTerrains(terrainLayer);
  }

  private Iterable<Terrain> createTerrains(TiledMapTileLayer terrainLayer) {
    Preconditions.checkNotNull(terrainLayer);
    Preconditions.checkArgument(terrainLayer.getWidth() > 0);
    Preconditions.checkArgument(terrainLayer.getHeight() > 0);
    List<Terrain> terrains = new ArrayList<>(terrainLayer.getWidth() * terrainLayer.getHeight());
    for (int y = 0; y < terrainLayer.getHeight(); y++) {
      for (int x = 0; x < terrainLayer.getWidth(); x++) {
        TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
        terrains.add(createTerrain(cell));
      }
    }
    return terrains;
  }

  private Terrain createTerrain(TiledMapTileLayer.Cell cell) {
    MapProperties tileProperties = cell.getTile().getProperties();
    if (tileProperties.containsKey(dataConfig.getTerrainTypeKey())) {
      String type = tileProperties.get(dataConfig.getTerrainTypeKey(), String.class);
      switch (type) {
        case "OBSTRUCTED":
          return new Obstructed();
        case "WATER":
          return new Water();
        default:
          throw new IllegalArgumentException("Unrecognized terrain type: " + type);
      }
    }
    return new Land();
  }
}
