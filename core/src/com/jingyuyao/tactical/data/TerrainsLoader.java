package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Terrain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
@Singleton
class TerrainsLoader {

  private final DataConfig dataConfig;
  private final Files files;
  private final InitLoader initLoader;

  @Inject
  TerrainsLoader(DataConfig dataConfig, Files files, InitLoader initLoader) {
    this.dataConfig = dataConfig;
    this.files = files;
    this.initLoader = initLoader;
  }

  /**
   * Loads the given level as a map of coordinates to terrains. Makes several assumptions such as
   * all layers are the same size with no offset, tilesets are embedded, only tile properties in
   * foreground layer is used for terrain type and embedded tilesets must have the same name as the
   * image file.
   */
  Map<Coordinate, Terrain> load(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelTerrainFileName(level));
    Terrains terrains = initLoader.fromJson(fileHandle.reader(), Terrains.class);
    int layerDataSize = terrains.layers.get(0).data.size();
    int layerHeight = terrains.layers.get(0).height;
    int layerWidth = terrains.layers.get(0).width;

    Map<TileSet, KeyBundle> cachedBundles = new HashMap<>();
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    for (int i = 0; i < layerDataSize; i++) {
      int x = i % layerWidth;
      int y = i / layerWidth;
      // Our world points top-right, terrain data points down-right
      Coordinate coordinate = new Coordinate(x, layerHeight - y - 1);

      List<IntKey> textures = new ArrayList<>();
      TileProperty tileProperty = null;
      for (Layer layer : terrains.layers) {
        int globalTileId = layer.data.get(i);
        for (TileSet tileSet : terrains.getTileset(globalTileId).asSet()) {
          int localTileId = tileSet.getLocalTileId(globalTileId);
          KeyBundle keyBundle;
          if (cachedBundles.containsKey(tileSet)) {
            keyBundle = cachedBundles.get(tileSet);
          } else {
            keyBundle = KeyBundle.tileset(tileSet.name);
            cachedBundles.put(tileSet, keyBundle);
          }
          textures.add(keyBundle.get(localTileId));
          if (tileSet.tileproperties.containsKey(localTileId)) {
            tileProperty = tileSet.tileproperties.get(localTileId);
          }
        }
      }
      terrainMap.put(coordinate, createTerrain(textures, tileProperty));
    }
    return terrainMap;
  }

  private Terrain createTerrain(List<IntKey> textures, TileProperty tileProperty) {
    if (tileProperty == null) {
      return new Terrain("space", textures, true, 1);
    }
    return new Terrain(tileProperty.name, textures, tileProperty.holdShip, tileProperty.moveCost);
  }

  // Minimum usable representation of the TMX json format. Assumes all tilesets are in the
  // "tilesets/" directory, there is only one tile layer and one tileset.
  private static class Terrains {

    private List<Layer> layers;
    private List<TileSet> tilesets;

    private Optional<TileSet> getTileset(int globalTileId) {
      for (TileSet tileSet : tilesets) {
        if (tileSet.hasGlobalTileId(globalTileId)) {
          return Optional.of(tileSet);
        }
      }
      return Optional.absent();
    }
  }

  private static class Layer {

    private List<Integer> data;
    private int width;
    private int height;
  }

  private static class TileSet {

    private int firstgid;
    private int tilecount;
    private String name;
    private Map<Integer, TileProperty> tileproperties = new HashMap<>();

    private boolean hasGlobalTileId(int globalTileId) {
      return globalTileId >= firstgid && globalTileId < firstgid + tilecount;
    }

    private int getLocalTileId(int globalTileId) {
      return globalTileId - firstgid;
    }
  }

  private static class TileProperty {

    private String name = "space";
    private boolean holdShip = true;
    private int moveCost = 1;
  }
}
