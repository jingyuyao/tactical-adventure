package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A terrain tile.
 */
class Terrain extends MapActor {
    private Type type;

    Terrain(Map map, TiledMapTileLayer.Cell cell,
            HighlightRenderer highlightRenderer, HighlightListener highlightListener,
            float x, float y, float width, float height,
            Type type) {
        super(map, cell, highlightRenderer, highlightListener, x, y, width, height);
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
