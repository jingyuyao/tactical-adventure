package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A terrain tile.
 */
class Terrain extends MapActor {
    private final TiledMapTileLayer.Cell cell;
    private Type type;

    Terrain(Map map, HighlightRenderer highlightRenderer, HighlightListener highlightListener,
            float x, float y, float width, float height,
            TiledMapTileLayer.Cell cell, Type type) {
        super(map, highlightRenderer, highlightListener, x, y, width, height);
        this.cell = cell;
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    TiledMapTileLayer.Cell getCell() {
        return cell;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
