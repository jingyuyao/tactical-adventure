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
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.DirectionalWeaponData;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.GrenadeData;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.HealData;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.state.Waiting;
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
  private final ItemFactory itemFactory;
  private final Gson gson;
  private final AssetManager assetManager;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final TerrainLoader terrainLoader;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      ItemFactory itemFactory,
      Gson gson,
      AssetManager assetManager,
      OrthogonalTiledMapRenderer mapRenderer,
      TerrainLoader terrainLoader) {
    this.model = model;
    this.waitingProvider = waitingProvider;
    this.characterFactory = characterFactory;
    this.itemFactory = itemFactory;
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
    String className = playerSave.getClassName();
    List<Item> items = createItems(playerSave.getItems());
    Player player;
    if (BasePlayer.class.getSimpleName().equals(className)) {
      player = characterFactory.createBasePlayer(playerSave.getData(), items);
    } else {
      throw new IllegalArgumentException("Unknown player class name: " + className);
    }
    return player;
  }

  private Enemy createEnemy(EnemySave enemySave) {
    String className = enemySave.getClassName();
    List<Item> items = createItems(enemySave.getItems());
    Enemy enemy;
    if (PassiveEnemy.class.getSimpleName().equals(className)) {
      enemy = characterFactory.createPassiveEnemy(enemySave.getData(), items);
    } else {
      throw new IllegalArgumentException("Unknown enemy class name: " + className);
    }
    return enemy;
  }

  private List<Item> createItems(Iterable<ItemSave> itemSaves) {
    List<Item> items = new ArrayList<Item>();
    if (itemSaves != null) {
      for (ItemSave itemSave : itemSaves) {
        items.add(createItem(itemSave));
      }
    }
    return items;
  }

  private Item createItem(ItemSave itemSave) {
    String className = itemSave.getClassName();
    if (DirectionalWeapon.class.getSimpleName().equals(className)) {
      return itemFactory.createDirectionalWeapon(
          itemSave.getData(gson, DirectionalWeaponData.class));
    } else if (Grenade.class.getSimpleName().equals(className)) {
      return itemFactory.createGrenade(itemSave.getData(gson, GrenadeData.class));
    } else if (Heal.class.getSimpleName().equals(className)) {
      return itemFactory.createHeal(itemSave.getData(gson, HealData.class));
    }
    throw new IllegalArgumentException("Unknown item class name: " + className);
  }
}
