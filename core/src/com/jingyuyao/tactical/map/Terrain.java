package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * A terrain tile.
 */
public class Terrain extends Actor {
    private final TiledMapTileLayer.Cell cell;
    private Type type;

    Terrain(final TiledMapTileLayer.Cell cell, Type type, float x, float y, float width, float height) {
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

    private class TerrainListener extends ClickListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            cell.setTile(null);
            return false;
        }
    }
}
