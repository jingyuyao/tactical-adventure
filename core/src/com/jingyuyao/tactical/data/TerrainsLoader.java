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
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
@Singleton
class TerrainsLoader {

  private static final String DEFAULT_TERRAIN_NAME = "ground";
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
   * Copied from com.badlogic.gdx.maps.tiled.BaseTmxMapLoader
   */
  private static FileHandle getRelativeFileHandle(FileHandle file, String path) {
    StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
    FileHandle result = file.parent();
    while (tokenizer.hasMoreElements()) {
      String token = tokenizer.nextToken();
      if (token.equals("..")) {
        result = result.parent();
      } else {
        result = result.child(token);
      }
    }
    return result;
  }

  /**
   * Loads the given level as a map of coordinates to terrains. Makes several assumptions such as
   * all layers are the same size with no offset, tilesets are externally sourced and only tile
   * properties in the most foreground layer (that has some properties) is used for terrain data.
   */
  Map<Coordinate, Terrain> load(int level) {
    FileHandle terrainsHandle = files.internal(dataConfig.getLevelTerrainFileName(level));
    Terrains terrains = initLoader.fromJson(terrainsHandle.reader(), Terrains.class);
    int layerDataSize = terrains.layers.get(0).data.size();
    int layerHeight = terrains.layers.get(0).height;
    int layerWidth = terrains.layers.get(0).width;

    Map<Integer, TilesetSource> tilesetSourceMap =
        createTileSetSources(terrainsHandle, terrains.tilesets);
    Map<TilesetSource, KeyBundle> cachedBundles = new HashMap<>();
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
        for (Integer firstGlobalId : getFirstGlobalId(tilesetSourceMap, globalTileId).asSet()) {
          TilesetSource tilesetSource = tilesetSourceMap.get(firstGlobalId);
          int localTileId = globalTileId - firstGlobalId;
          KeyBundle keyBundle;
          if (cachedBundles.containsKey(tilesetSource)) {
            keyBundle = cachedBundles.get(tilesetSource);
          } else {
            keyBundle = KeyBundle.raw(
                tilesetSource.gdxImagePath, tilesetSource.gdxImageExtension);
            cachedBundles.put(tilesetSource, keyBundle);
          }
          textures.add(keyBundle.get(localTileId));
          if (tilesetSource.tileproperties.containsKey(localTileId)) {
            tileProperty = tilesetSource.tileproperties.get(localTileId);
          }
        }
      }
      terrainMap.put(coordinate, createTerrain(textures, tileProperty));
    }
    return terrainMap;
  }

  /**
   * Returns a map of the first global ID along with its tileset.
   */
  private Map<Integer, TilesetSource> createTileSetSources(
      FileHandle terrainsFileHandle, List<TilesetRef> tilesetRefs) {
    Map<Integer, TilesetSource> tilesetSourceMap = new TreeMap<>();
    for (TilesetRef tilesetRef : tilesetRefs) {
      FileHandle sourceHandle = getRelativeFileHandle(terrainsFileHandle, tilesetRef.source);
      TilesetSource source = initLoader.fromJson(sourceHandle.reader(), TilesetSource.class);
      FileHandle sourceImageHandle = getRelativeFileHandle(sourceHandle, source.image);
      source.gdxImagePath = sourceImageHandle.pathWithoutExtension();
      source.gdxImageExtension = sourceImageHandle.extension();
      tilesetSourceMap.put(tilesetRef.firstgid, source);
    }
    return tilesetSourceMap;
  }

  private Optional<Integer> getFirstGlobalId(
      Map<Integer, TilesetSource> tilesetSources, int globalTileId) {
    for (Entry<Integer, TilesetSource> entry : tilesetSources.entrySet()) {
      int firstGlobalId = entry.getKey();
      int tileCount = entry.getValue().tilecount;
      if (globalTileId >= firstGlobalId && globalTileId < firstGlobalId + tileCount) {
        return Optional.of(entry.getKey());
      }
    }
    return Optional.absent();
  }

  private Terrain createTerrain(List<IntKey> textures, TileProperty tileProperty) {
    if (tileProperty == null) {
      return new Terrain(DEFAULT_TERRAIN_NAME, textures, true, 1);
    }
    return new Terrain(tileProperty.name, textures, tileProperty.holdShip, tileProperty.moveCost);
  }

  // Minimum usable representation of the TMX json format.
  private static class Terrains {

    private List<Layer> layers;
    /**
     * Only allows referencing external tilesets.
     */
    private List<TilesetRef> tilesets;
  }

  private static class Layer {

    private List<Integer> data;
    private int width;
    private int height;
  }

  private static class TilesetRef {

    private int firstgid;
    private String source;
  }

  private static class TilesetSource {

    private String image;
    private int tilecount;
    private Map<Integer, TileProperty> tileproperties = new HashMap<>();
    /**
     * Not part of Tmx spec, should be calculated manually.
     */
    private String gdxImagePath = "placeholder value";
    /**
     * Not part of Tmx spec, should be calculated manually.
     */
    private String gdxImageExtension = "placeholder value";
  }

  private static class TileProperty {

    private String name = DEFAULT_TERRAIN_NAME;
    private boolean holdShip = true;
    private int moveCost = 1;
  }
}
