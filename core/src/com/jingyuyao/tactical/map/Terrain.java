package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * A terrain tile.
 */
public class Terrain extends Actor {
    private final Map map;
    private final TiledMapTileLayer.Cell cell;
    private Type type;

    Terrain(Map map, TiledMapTileLayer.Cell cell, Type type, float x, float y, float width, float height) {
        this.map = map;
        this.cell = cell;
        this.type = type;
        setBounds(x, y, width, height);
        addListener(this.new TerrainListener());
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    private class TerrainListener extends InputListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return super.touchDown(event, x, y, pointer, button);
        }
    }
}
