package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.ModelManager;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.*;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.ItemFactory;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapFactory;
import com.jingyuyao.tactical.model.map.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class MapLoader {
    private static final String TERRAIN_LAYER = "terrain";
    private static final String TERRAIN_TYPE_KEY = "type";

    private final ModelManager modelManager;
    private final CharacterFactory characterFactory;
    private final MapFactory mapFactory;
    private final ItemFactory itemFactory;
    private final OrthogonalTiledMapRenderer mapRenderer;

    @Inject
    MapLoader(
            ModelManager modelManager,
            CharacterFactory characterFactory,
            MapFactory mapFactory,
            ItemFactory itemFactory,
            OrthogonalTiledMapRenderer mapRenderer
    ) {
        this.modelManager = modelManager;
        this.characterFactory = characterFactory;
        this.mapFactory = mapFactory;
        this.itemFactory = itemFactory;
        this.mapRenderer = mapRenderer;
    }

    public void loadMap(TiledMap tiledMap) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        List<Terrain> terrains = new ArrayList<Terrain>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                terrains.add(createTerrain(x, y, cell));
            }
        }

        Iterable<Character> characters = Iterables.concat(createTestPlayers(), createTestEnemies());

        modelManager.initializeMap(characters, terrains);
        mapRenderer.setMap(tiledMap);
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
        return mapFactory.createTerrain(x, y, type);
    }

    // TODO: remove us
    private Iterable<Player> createTestPlayers() {
        int hp = 20;
        return ImmutableList.of(
                characterFactory.createPlayer(new Coordinate(2, 2), "john", new Stats(hp, 5, normalAndObstructed()), createItems1()),
                characterFactory.createPlayer(new Coordinate(2, 3), "john", new Stats(hp, 6, normalAndObstructed()), createItems2())
        );
    }

    private Iterable<Enemy> createTestEnemies() {
        int hp = 20;
        return ImmutableList.of(
                characterFactory.createEnemy(new Coordinate(8, 3), "billy", new Stats(hp, 3, normalAndObstructed()), createItems1()),
                characterFactory.createEnemy(new Coordinate(9, 3), "billy", new Stats(hp, 2, normalAndObstructed()), createItems1())
        );
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
        weapons.add(itemFactory.createWeapon("Axe", 1, attackPower, ImmutableSet.of(1)));
        weapons.add(itemFactory.createWeapon("Sword", 10, attackPower, ImmutableSet.of(1)));
        weapons.add(itemFactory.createWeapon("Bow", 3, attackPower, ImmutableSet.of(2)));
        return characterFactory.createItems(
                weapons,
                Lists.<Consumable>newArrayList(itemFactory.createHeal("pot", 3)));
    }

    private Items createItems2() {
        int attackPower = 3;
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(itemFactory.createWeapon("Bow", 5, attackPower, ImmutableSet.of(2)));
        return characterFactory.createItems(weapons, Collections.<Consumable>emptyList());
    }
}
