package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A terrain tile.
 */
class Terrain extends Actor {
    private final Map map;
    private final TiledMapTileLayer.Cell cell;
    private final HighlightRenderer highlightRenderer;
    private final HighlightListener highlightListener;
    private Type type;

    Terrain(Map map, TiledMapTileLayer.Cell cell, Type type,
            HighlightRenderer highlightRenderer, HighlightListener highlightListener,
            float x, float y, float width, float height) {
        this.map = map;
        this.cell = cell;
        this.type = type;
        this.highlightRenderer = highlightRenderer;
        this.highlightListener = highlightListener;
        setBounds(x, y, width, height);
        addListener(highlightListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (highlightListener.isHighlighted()) {
            highlightRenderer.draw(this, batch);
        }
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
