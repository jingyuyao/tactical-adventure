package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.state.Waiting;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MapLoader {

  private static final String TERRAIN_LAYER = "terrain";

  private final Model model;
  private final Provider<Waiting> waitingProvider;
  private final CharacterFactory characterFactory;
  private final Gson gson;
  private final AssetManager assetManager;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final TerrainLoader terrainLoader;
  private final ItemLoader itemLoader;
  private final PlayerLoader playerLoader;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      Gson gson,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer mapRenderer,
      TerrainLoader terrainLoader,
      ItemLoader itemLoader, PlayerLoader playerLoader) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.characterFactory = characterFactory;
    this.gson = gson;
    this.assetManager = assetManager;
    this.mapRenderer = mapRenderer;
    this.terrainLoader = terrainLoader;
    this.itemLoader = itemLoader;
    this.playerLoader = playerLoader;
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
        terrainLoader.createTerrains(terrainLayer, width, height),
        playerLoader.createPlayers(mapSave.getPlayers()),
        createEnemies(mapSave.getEnemies()),
        waitingProvider.get());
    mapRenderer.setMap(tiledMap);
  }

  private Iterable<Enemy> createEnemies(List<EnemySave> enemySaves) {
    ImmutableList.Builder<Enemy> builder = ImmutableList.builder();
    for (EnemySave enemySave : enemySaves) {
      builder.add(createEnemy(enemySave));
    }
    return builder.build();
  }

  private Enemy createEnemy(EnemySave enemySave) {
    String className = enemySave.getClassName();
    List<Item> items = itemLoader.createItems(enemySave.getItems());
    Enemy enemy;
    if (PassiveEnemy.class.getSimpleName().equals(className)) {
      enemy = characterFactory.createPassiveEnemy(enemySave.getData(), items);
    } else {
      throw new IllegalArgumentException("Unknown enemy class name: " + className);
    }
    return enemy;
  }
}
