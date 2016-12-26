package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.*;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.*;

public class LevelLoader {
    private static final String TERRAIN_LAYER = "terrain";
    private static final String TERRAIN_TYPE_KEY = "type";

    public static Level loadLevel(TiledMap tiledMap) {
        AnimationCounter animationCounter = new AnimationCounter();
        Map map = createMap(tiledMap, animationCounter);
        Turn turn = new Turn(map);
        MapState mapState = new MapState(map, turn, animationCounter);
        Highlighter highlighter = new Highlighter();
        return new Level(map, mapState, turn, highlighter, animationCounter);
    }

    private static Map createMap(TiledMap tiledMap, AnimationCounter animationCounter) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        Map map = new Map(width, height, animationCounter);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                Terrain terrain = createTerrain(x, y, cell);
                map.getTerrains().set(x, y, terrain);
            }
        }

        addTestCharacters(map);

        return map;
    }

    private static Terrain createTerrain(int x, int y, TiledMapTileLayer.Cell cell) {
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
        return new Terrain(x, y, type);
    }

    // TODO: remove us
    private static void addTestCharacters(Map map) {
        int hp = 20;

        map.add(new Player(5, 5, "john", new Stats(hp, 5, normalAndObstructed()), createItems1()));
        map.add(new Player(5, 6, "john", new Stats(hp, 6, normalAndObstructed()), createItems2()));
        map.add(new Enemy(10, 10, "billy", new Stats(hp, 3, normalAndObstructed()), createItems1()));
        map.add(new Enemy(11, 7, "billy", new Stats(hp, 2, normalAndObstructed()), createItems1()));
    }

    private static Set<Terrain.Type> normalAndObstructed() {
        Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
        standOnTerrainTypes.add(Terrain.Type.NORMAL);
        standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
        return standOnTerrainTypes;
    }

    private static Items createItems1() {
        int attackPower = 5;
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon(0, "Axe", 1, attackPower, ImmutableSet.of(1)));
        weapons.add(new Weapon(1, "Sword", 10, attackPower, ImmutableSet.of(1)));
        weapons.add(new Weapon(2, "Bow", 3, attackPower, ImmutableSet.of(2)));
        return new Items(weapons, Lists.<Consumable>newArrayList(new Heal(0, "pot", 3)));
    }

    private static Items createItems2() {
        int attackPower = 3;
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon(2, "Bow", 5, attackPower, ImmutableSet.of(2)));
        return new Items(weapons, Collections.<Consumable>emptyList());
    }
}
