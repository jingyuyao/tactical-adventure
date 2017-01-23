package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.Stats;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.item.ItemStats;
import com.jingyuyao.tactical.model.item.WeaponStats;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.TerrainFactory;
import com.jingyuyao.tactical.model.state.Waiting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MapLoader {

  private static final String TERRAIN_LAYER = "terrain";
  private static final String TERRAIN_TYPE_KEY = "type";

  private final EventBus eventBus;
  private final Provider<Waiting> waitingProvider;
  private final CharacterFactory characterFactory;
  private final TerrainFactory terrainFactory;
  private final ItemFactory itemFactory;
  private final OrthogonalTiledMapRenderer mapRenderer;

  @Inject
  MapLoader(
      EventBus eventBus,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      TerrainFactory terrainFactory,
      ItemFactory itemFactory,
      OrthogonalTiledMapRenderer mapRenderer) {
    this.eventBus = eventBus;
    this.waitingProvider = waitingProvider;
    this.characterFactory = characterFactory;
    this.terrainFactory = terrainFactory;
    this.itemFactory = itemFactory;
    this.mapRenderer = mapRenderer;
  }

  public void loadMap(TiledMap tiledMap) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
    Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

    int height = terrainLayer.getHeight();
    int width = terrainLayer.getWidth();
    Preconditions.checkArgument(height > 0, "MapView height must be > 0");
    Preconditions.checkArgument(width > 0, "MapView width must be > 0");

    eventBus.post(
        new NewMap(
            width,
            height,
            createTestPlayers(),
            createTestEnemies(),
            createTerrains(terrainLayer, width, height),
            waitingProvider.get()));
    mapRenderer.setMap(tiledMap);
  }

  private List<Terrain> createTerrains(TiledMapTileLayer terrainLayer, int width, int height) {
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
    MapProperties tileProperties = cell.getTile().getProperties();
    Terrain.Type type = Terrain.Type.NORMAL;
    if (tileProperties.containsKey(TERRAIN_TYPE_KEY)) {
      String tileType = tileProperties.get(TERRAIN_TYPE_KEY, String.class);
      try {
        type = Terrain.Type.valueOf(tileType);
      } catch (IllegalArgumentException e) {
        Gdx.app.log("Terrain", String.format("invalid type %s", tileType));
      }
    }
    return terrainFactory.create(new Coordinate(x, y), type);
  }

  // TODO: remove us
  private List<Player> createTestPlayers() {
    int hp = 20;
    return ImmutableList.of(
        characterFactory.createPlayer(
            new Coordinate(2, 2), new Stats("john", hp, 5, normalAndObstructed()), createItems1()),
        characterFactory.createPlayer(
            new Coordinate(2, 3), new Stats("john", hp, 6, normalAndObstructed()), createItems2()));
  }

  private List<Enemy> createTestEnemies() {
    int hp = 20;
    return ImmutableList.of(
        characterFactory.createPassiveEnemy(
            new Coordinate(8, 3), new Stats("billy", hp, 3, normalAndObstructed()),
            createItems1()),
        characterFactory.createPassiveEnemy(
            new Coordinate(9, 3),
            new Stats("billy", hp, 2, normalAndObstructed()),
            createItems2()));
  }

  private Set<Terrain.Type> normalAndObstructed() {
    Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
    standOnTerrainTypes.add(Terrain.Type.NORMAL);
    standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
    return standOnTerrainTypes;
  }

  private List<Item> createItems1() {
    int attackPower = 5;
    List<Item> items = new ArrayList<Item>();
    items.add(itemFactory.createPiercingLaser(new WeaponStats("Laser5", 1, attackPower)));
    items.add(itemFactory.createMelee(new WeaponStats("Melee5", 10, attackPower)));
    items.add(itemFactory.createMelee(new WeaponStats("Melee5", 3, attackPower)));
    items.add(itemFactory.createHeal(new ItemStats("pot", 3)));
    return items;
  }

  private List<Item> createItems2() {
    int attackPower = 3;
    List<Item> weapons = new ArrayList<Item>();
    weapons.add(itemFactory.createPiercingLaser(new WeaponStats("Laser3", 5, attackPower)));
    return weapons;
  }
}
