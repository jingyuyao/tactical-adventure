package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.*;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Markings;
import com.jingyuyao.tactical.model.state.Waiting;

import java.util.*;

public class LevelLoader {
    private static final String TERRAIN_LAYER = "terrain";
    private static final String TERRAIN_TYPE_KEY = "type";

    public static Level loadLevel(EventBus eventBus, TiledMap tiledMap) {
        // TODO: HOHOHO connect all of these to guice!
        Map map = createMap(eventBus, tiledMap);
        Turn turn = new Turn(eventBus, map);
        Waiter waiter = new Waiter(eventBus, new LinkedList<Runnable>());
        MarkingFactory markingFactory = new MarkingFactory(eventBus, map, waiter);
        TargetInfoFactory targetInfoFactory = new TargetInfoFactory(map);
        AttackPlanFactory attackPlanFactory = new AttackPlanFactory(targetInfoFactory);
        Markings markings = new Markings(eventBus, markingFactory, new HashMap<Character, Marking>());
        Waiting initialState = new Waiting(eventBus, markings, targetInfoFactory, attackPlanFactory);
        MapState mapState = new MapState(eventBus, waiter, initialState);
        Highlighter highlighter = new Highlighter(eventBus, map);
        return new Level(map, mapState, turn, highlighter, waiter);
    }

    private static Map createMap(EventBus eventBus, TiledMap tiledMap) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        Grid<Terrain> terrains = new Grid<Terrain>(eventBus, HashBasedTable.<Integer, Integer, Terrain>create());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                terrains.put(x, y, createTerrain(eventBus, x, y, cell));
            }
        }

        CharacterContainer<Player> players = new CharacterContainer<Player>(eventBus, createTestPlayers(eventBus));
        CharacterContainer<Enemy> enemies = new CharacterContainer<Enemy>(eventBus, createTestEnemies(eventBus));
        return new Map(terrains, players, enemies);
    }

    private static Terrain createTerrain(EventBus eventBus, int x, int y, TiledMapTileLayer.Cell cell) {
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
        return new Terrain(eventBus, x, y, type);
    }

    // TODO: remove us
    private static Iterable<Player> createTestPlayers(EventBus eventBus) {
        int hp = 20;
        return ImmutableSet.of(
                new Player(eventBus, 2, 2, "john", new Stats(hp, 5, normalAndObstructed()), createItems1(eventBus)),
                new Player(eventBus, 2, 3, "john", new Stats(hp, 6, normalAndObstructed()), createItems2(eventBus))
        );
    }

    private static Iterable<Enemy> createTestEnemies(EventBus eventBus) {
        int hp = 20;
        return ImmutableSet.of(
                new Enemy(eventBus, 8, 3, "billy", new Stats(hp, 3, normalAndObstructed()), createItems1(eventBus)),
                new Enemy(eventBus, 9, 4, "billy", new Stats(hp, 2, normalAndObstructed()), createItems1(eventBus))
        );
    }

    private static Set<Terrain.Type> normalAndObstructed() {
        Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
        standOnTerrainTypes.add(Terrain.Type.NORMAL);
        standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
        return standOnTerrainTypes;
    }

    private static Items createItems1(EventBus eventBus) {
        int attackPower = 5;
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon(eventBus, 0, "Axe", 1, attackPower, ImmutableSet.of(1)));
        weapons.add(new Weapon(eventBus, 1, "Sword", 10, attackPower, ImmutableSet.of(1)));
        weapons.add(new Weapon(eventBus, 2, "Bow", 3, attackPower, ImmutableSet.of(2)));
        return new Items(eventBus, weapons, Lists.<Consumable>newArrayList(new Heal(eventBus, 0, "pot", 3)));
    }

    private static Items createItems2(EventBus eventBus) {
        int attackPower = 3;
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon(eventBus, 2, "Bow", 5, attackPower, ImmutableSet.of(2)));
        return new Items(eventBus, weapons, Collections.<Consumable>emptyList());
    }
}
