package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.gson.Gson;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.state.Waiting;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MapLoader {

  private static final String TERRAIN_LAYER = "terrain";

  private final Model model;
  private final Provider<Waiting> waitingProvider;
  private final Gson gson;
  private final AssetManager assetManager;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final TerrainLoader terrainLoader;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      Gson gson,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer mapRenderer,
      TerrainLoader terrainLoader) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.gson = gson;
    this.assetManager = assetManager;
    this.mapRenderer = mapRenderer;
    this.terrainLoader = terrainLoader;
  }

  public void loadMap(String mapName) {
    assetManager.load(mapName + ".tmx", TiledMap.class);
    assetManager.finishLoading();
    TiledMap tiledMap = assetManager.get(mapName + ".tmx", TiledMap.class);

    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
    Preconditions.checkNotNull(terrainLayer);

    int height = terrainLayer.getHeight();
    int width = terrainLayer.getWidth();
    Preconditions.checkArgument(height > 0);
    Preconditions.checkArgument(width > 0);

    FileHandle mapData = Gdx.files.internal(mapName + ".json");
    MapSave mapSave = gson.fromJson(mapData.readString(), MapSave.class);

    FluentIterable<Character> characters = mapSave.getCharacters();

    model.loadMap(
        width,
        height,
        terrainLoader.createTerrains(terrainLayer, width, height),
        characters.filter(Player.class),
        characters.filter(Enemy.class),
        waitingProvider.get());
    mapRenderer.setMap(tiledMap);
  }
}
