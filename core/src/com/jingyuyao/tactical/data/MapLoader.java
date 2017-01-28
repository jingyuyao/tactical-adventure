package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.terrain.TerrainFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MapLoader {

  private static final String TERRAIN_LAYER = "terrain";
  private static final String TERRAIN_TYPE_KEY = "type";

  private final Model model;
  private final Provider<Waiting> waitingProvider;
  private final CharacterFactory characterFactory;
  private final TerrainFactory terrainFactory;
  private final ItemFactory itemFactory;
  private final Gson gson;
  private final AssetManager assetManager;
  private final OrthogonalTiledMapRenderer mapRenderer;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      TerrainFactory terrainFactory,
      ItemFactory itemFactory,
      Gson gson,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer mapRenderer) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.characterFactory = characterFactory;
    this.terrainFactory = terrainFactory;
    this.itemFactory = itemFactory;
    this.gson = gson;
    this.assetManager = assetManager;
    this.mapRenderer = mapRenderer;
  }

  public void loadMap(String mapName) {
    assetManager.load(mapName + ".tmx", TiledMap.class);
    assetManager.finishLoading();
    TiledMap tiledMap = assetManager.get(mapName + ".tmx", TiledMap.class);

    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
    Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

    int height = terrainLayer.getHeight();
    int width = terrainLayer.getWidth();
    Preconditions.checkArgument(height > 0, "MapView height must be > 0");
    Preconditions.checkArgument(width > 0, "MapView width must be > 0");

    FileHandle mapData = Gdx.files.internal(mapName + ".json");
    MapSave mapSave = gson.fromJson(mapData.readString(), MapSave.class);

    model.newMap(
        width,
        height,
        createTerrains(terrainLayer, width, height),
        createPlayers(mapSave.getPlayers()),
        createEnemies(mapSave.getEnemies()),
        waitingProvider.get());
    mapRenderer.setMap(tiledMap);
  }

  private Iterable<Player> createPlayers(List<PlayerSave> playerSaves) {
    ImmutableList.Builder<Player> builder = ImmutableList.builder();
    for (PlayerSave playerSave : playerSaves) {
      builder.add(createPlayer(playerSave));
    }
    return builder.build();
  }

  private Iterable<Enemy> createEnemies(List<EnemySave> enemySaves) {
    ImmutableList.Builder<Enemy> builder = ImmutableList.builder();
    for (EnemySave enemySave : enemySaves) {
      builder.add(createEnemy(enemySave));
    }
    return builder.build();
  }

  private Player createPlayer(PlayerSave playerSave) {
    // TODO: add items
    return characterFactory.createPlayer(playerSave.getPlayer());
  }

  private Enemy createEnemy(EnemySave enemySave) {
    // TODO: add items
    return characterFactory.createPassiveEnemy(enemySave.getEnemy());
  }

  private Iterable<Terrain> createTerrains(TiledMapTileLayer terrainLayer, int width, int height) {
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
        Gdx.app.error("MapLoader", "Unrecognized terrain type: " + type);
      }
    }

    return terrainFactory.createLand(data);
  }
}
