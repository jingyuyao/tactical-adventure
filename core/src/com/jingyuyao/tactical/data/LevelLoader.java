package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.state.MapState;

public class LevelLoader {
    private static final String TERRAIN_LAYER = "terrain";
    private static final String TERRAIN_TYPE_KEY = "type";

    public static Level loadLevel(TiledMap tiledMap) {
        Map map = createMap(tiledMap);
        Turn turn = new Turn(map);
        AnimationCounter animationCounter = new AnimationCounter();
        MapState mapState = new MapState(map, turn, animationCounter);
        return new Level(map, mapState, turn, animationCounter);
    }

    private static Map createMap(TiledMap tiledMap) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        Map map = new Map(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                Terrain terrain = createTerrain(x, y, cell);
                map.getTerrains().set(x, y, terrain);
            }
        }

        // Testing
        map.addCharacter(new Player(5, 5, "john", 5));
        map.addCharacter(new Player(5, 6, "john", 6));
        map.addCharacter(new Enemy(10, 10, "billy", 3));
        map.addCharacter(new Enemy(15, 10, "billy", 2));

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
}
