package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Terrain;
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

  Map<Coordinate, Terrain> load(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelTerrainFileName(level));
    Terrains terrains = initLoader.fromJson(fileHandle.reader(), Terrains.class);
    Preconditions.checkArgument(terrains.layers.size() == 1);
    Preconditions.checkArgument(terrains.tilesets.size() == 1);
    Layer layer = terrains.layers.get(0);
    TileSet tileSet = terrains.tilesets.get(0);
    KeyBundle bundle = KeyBundle.tileset(tileSet.name);

    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    for (int i = 0; i < layer.data.size(); i++) {
      int x = i % layer.width;
      int y = i / layer.width;
      int tileId = layer.data.get(i);
      // Our world points top-right, terrain data points down-right
      Coordinate coordinate = new Coordinate(x, layer.height - y - 1);
      IntKey key = bundle.get(tileId);
      // tile property is zero indexed but tile id is one indexed... wtf
      terrainMap.put(coordinate, createTerrain(key, tileSet.tileproperties.get(tileId - 1)));
    }
    return terrainMap;
  }

  private Terrain createTerrain(IntKey key, TileProperty tileProperty) {
    if (tileProperty == null) {
      return new Terrain("space", key, true, 1);
    }
    return new Terrain(tileProperty.name, key, tileProperty.holdShip, tileProperty.moveCost);
  }

  // Minimum usable representation of the TMX json format. Assumes all tilesets are in the
  // "tilesets/" directory, there is only one tile layer and one tileset.
  private static class Terrains {

    private List<Layer> layers;
    private List<TileSet> tilesets;
  }

  private static class Layer {

    private List<Integer> data;
    private int width;
    private int height;
  }

  private static class TileSet {

    private String name;
    private Map<Integer, TileProperty> tileproperties = new HashMap<>();
  }

  private static class TileProperty {

    private String name = "space";
    private boolean holdShip = true;
    private int moveCost = 1;
  }
}
