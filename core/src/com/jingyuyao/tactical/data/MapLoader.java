package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
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
  private final CharacterLoader characterLoader;
  private final TerrainLoader terrainLoader;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      Gson gson,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer mapRenderer,
      CharacterLoader characterLoader,
      TerrainLoader terrainLoader) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.gson = gson;
    this.assetManager = assetManager;
    this.mapRenderer = mapRenderer;
    this.characterLoader = characterLoader;
    this.terrainLoader = terrainLoader;
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

    Iterable<Character> characters = characterLoader.createCharacter(mapSave.getCharacterSaves());

    model.newMap(
        width,
        height,
        terrainLoader.createTerrains(terrainLayer, width, height),
        Iterables.filter(characters, Player.class),
        Iterables.filter(characters, Enemy.class),
        waitingProvider.get());
    mapRenderer.setMap(tiledMap);
  }
}
