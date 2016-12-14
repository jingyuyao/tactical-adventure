package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class MapActor extends Actor {
    private final Map map;

    private final HighlightRenderer highlightRenderer;
    private final HighlightListener highlightListener;

    MapActor(Map map,
             HighlightRenderer highlightRenderer, HighlightListener highlightListener,
             float x, float y, float width, float height) {
        this.map = map;
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

    protected Map getMap() {
        return map;
    }
}
