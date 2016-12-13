package com.jingyuyao.tactical.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import javax.inject.Inject;

class TerrainFactory {
    private static final String TYPE_KEY = "type";

    private final ShapeRenderer shapeRenderer;

    @Inject
    TerrainFactory(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    Terrain create(Map map, TiledMapTileLayer.Cell cell, float x, float y, float width, float height) {
        MapProperties tileProperties = cell.getTile().getProperties();
        Terrain.Type type = Terrain.Type.NORMAL;
        if (tileProperties.containsKey(TYPE_KEY)) {
            String tileType = tileProperties.get(TYPE_KEY, String.class);
            try {
                type = Terrain.Type.valueOf(tileType);
            } catch (IllegalArgumentException e) {
                Gdx.app.log("Terrain", String.format("invalid type %s", tileType));
            }
        }
        return new Terrain(map, cell, type, shapeRenderer, x, y, width, height);
    }
}
