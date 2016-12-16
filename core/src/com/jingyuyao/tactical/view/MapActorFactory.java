package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.jingyuyao.tactical.controller.HighlightListener;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;
import javax.inject.Provider;

public class MapActorFactory {
    private static final String TYPE_KEY = "type";

    private final Provider<HighlightListener> highlightListenerProvider;
    private final ShapeRenderer shapeRenderer;

    @Inject
    public MapActorFactory(Provider<HighlightListener> highlightListenerProvider, ShapeRenderer shapeRenderer) {
        this.highlightListenerProvider = highlightListenerProvider;
        this.shapeRenderer = shapeRenderer;
    }

    MapActor<Character> createCharacter(int x, int y, float size) {
        return new MapActor<Character>(new Character(x, y), size, highlightListenerProvider.get(), shapeRenderer);
    }

    MapActor<Terrain> createTerrain(TiledMapTileLayer.Cell cell, int x, int y, float size) {
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

        return new MapActor<Terrain>(new Terrain(x, y, type), size, highlightListenerProvider.get(), shapeRenderer);
    }
}
