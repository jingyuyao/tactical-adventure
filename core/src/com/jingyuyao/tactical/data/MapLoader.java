package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.battle.ImmediateTargetFactory;
import com.jingyuyao.tactical.model.battle.PiercingTargetFactory;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Items;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.Stats;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.TerrainFactory;
import com.jingyuyao.tactical.model.state.Waiting;
import java.util.ArrayList;
import java.util.Collections;
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
  private final ImmediateTargetFactory immediateTargetFactory;
  private final PiercingTargetFactory piercingTargetFactory;
  private final OrthogonalTiledMapRenderer mapRenderer;

  @Inject
  MapLoader(
      EventBus eventBus,
      Provider<Waiting> waitingProvider,
      CharacterFactory characterFactory,
      TerrainFactory terrainFactory,
      ItemFactory itemFactory,
      ImmediateTargetFactory immediateTargetFactory,
      PiercingTargetFactory piercingTargetFactory,
      OrthogonalTiledMapRenderer mapRenderer) {
    this.eventBus = eventBus;
    this.waitingProvider = waitingProvider;
    this.characterFactory = characterFactory;
    this.terrainFactory = terrainFactory;
    this.itemFactory = itemFactory;
    this.immediateTargetFactory = immediateTargetFactory;
    this.piercingTargetFactory = piercingTargetFactory;
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
            new Coordinate(2, 2), "john", new Stats(hp, 5, normalAndObstructed()), createItems1()),
        characterFactory.createPlayer(
            new Coordinate(2, 3), "john", new Stats(hp, 6, normalAndObstructed()), createItems2()));
  }

  private List<Enemy> createTestEnemies() {
    int hp = 20;
    return ImmutableList.of(
        characterFactory.createEnemy(
            new Coordinate(8, 3), "billy", new Stats(hp, 3, normalAndObstructed()), createItems1()),
        characterFactory.createEnemy(
            new Coordinate(9, 3),
            "billy",
            new Stats(hp, 2, normalAndObstructed()),
            createItems2()));
  }

  private Set<Terrain.Type> normalAndObstructed() {
    Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
    standOnTerrainTypes.add(Terrain.Type.NORMAL);
    standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
    return standOnTerrainTypes;
  }

  private Items createItems1() {
    int attackPower = 5;
    List<Weapon> weapons = new ArrayList<Weapon>();
    weapons.add(itemFactory.createWeapon("Laser5", 1, attackPower, piercingTargetFactory));
    weapons.add(itemFactory.createWeapon("Melee5", 10, attackPower, immediateTargetFactory));
    weapons.add(itemFactory.createWeapon("Melee5", 3, attackPower, immediateTargetFactory));
    return characterFactory.createItems(
        weapons, Lists.<Consumable>newArrayList(itemFactory.createHeal("pot", 3)));
  }

  private Items createItems2() {
    int attackPower = 3;
    List<Weapon> weapons = new ArrayList<Weapon>();
    weapons.add(itemFactory.createWeapon("Laser3", 5, attackPower, piercingTargetFactory));
    return characterFactory.createItems(weapons, Collections.<Consumable>emptyList());
  }
}
