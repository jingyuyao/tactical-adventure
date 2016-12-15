package com.jingyuyao.tactical.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.jingyuyao.tactical.object.Terrain;

import javax.inject.Inject;
import javax.inject.Provider;

class TerrainFactory {
    private static final String TYPE_KEY = "type";

    private final Provider<Highlighter> highlighterProvider;

    @Inject
    TerrainFactory(Provider<Highlighter> highlighterProvider) {
        this.highlighterProvider = highlighterProvider;
    }

    MapActor<Terrain> create(TiledMapTileLayer.Cell cell, int x, int y, float size) {
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

        return new MapActor<Terrain>(new Terrain(x, y, type), size, highlighterProvider.get());
    }
}
