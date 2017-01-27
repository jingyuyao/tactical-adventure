package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.CharacterData;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.DirectionalWeaponData;
import com.jingyuyao.tactical.model.item.GrenadeData;
import com.jingyuyao.tactical.model.item.ItemData;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.map.Coordinate;
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

  private final Model model;
  private final Provider<Waiting> waitingProvider;
  private final CharacterFactory characterFactory;
  private final TerrainFactory terrainFactory;
  private final ItemFactory itemFactory;
  private final OrthogonalTiledMapRenderer mapRenderer;

  @Inject
  MapLoader(
      Model model,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      TerrainFactory terrainFactory,
      ItemFactory itemFactory,
      OrthogonalTiledMapRenderer mapRenderer) {
    this.model = model;
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

    model.newMap(
        width,
        height,
        createTerrains(terrainLayer, width, height),
        createTestPlayers(),
        createTestEnemies(),
        waitingProvider.get());
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
    Player p1 = characterFactory.createPlayer(
        new Coordinate(2, 2), new CharacterData("john", 20, 5, normalAndObstructed()));
    Player p2 = characterFactory.createPlayer(
        new Coordinate(2, 3), new CharacterData("john", 20, 6, normalAndObstructed()));
    addItems1(p1);
    addItems2(p2);
    return ImmutableList.of(p1, p2);
  }

  private List<Enemy> createTestEnemies() {
    Enemy e1 = characterFactory.createPassiveEnemy(
        new Coordinate(8, 3), new CharacterData("billy", 20, 3, normalAndObstructed()));
    Enemy e2 = characterFactory.createPassiveEnemy(
        new Coordinate(9, 3),
        new CharacterData("billy", 20, 2, normalAndObstructed()));
    addItems1(e1);
    addItems2(e2);
    return ImmutableList.of(e1, e2);
  }

  private Set<Terrain.Type> normalAndObstructed() {
    Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
    standOnTerrainTypes.add(Terrain.Type.NORMAL);
    standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
    return standOnTerrainTypes;
  }

  private void addItems1(Character owner) {
    owner.addItem(
        itemFactory
            .createDirectionalWeapon(owner, new DirectionalWeaponData("Laser5", 1, 5, 10)));
    owner.addItem(
        itemFactory
            .createDirectionalWeapon(owner, new DirectionalWeaponData("Melee5", 10, 5, 1)));
    owner.addItem(
        itemFactory
            .createDirectionalWeapon(owner, new DirectionalWeaponData("Melee10", 3, 10, 1)));
    owner.addItem(itemFactory.createHeal(owner, new ItemData("pot", 3)));
  }

  private void addItems2(Character owner) {
    owner.addItem(
        itemFactory
            .createDirectionalWeapon(owner, new DirectionalWeaponData("Laser3", 5, 3, 10)));
    owner.addItem(itemFactory.createGrenade(owner, new GrenadeData("Grenade5", 3, 5, 5, 3)));
  }
}
