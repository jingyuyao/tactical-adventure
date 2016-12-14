package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import javax.inject.Inject;

class CharacterFactory {
    private final HighlightRenderer highlightRenderer;
    private final ShapeRenderer shapeRenderer;

    @Inject
    CharacterFactory(HighlightRenderer highlightRenderer, ShapeRenderer shapeRenderer) {
        this.highlightRenderer = highlightRenderer;
        this.shapeRenderer = shapeRenderer;
    }

    Character create(Map map, float x, float y, float width, float height) {
        return new Character(map, highlightRenderer, new HighlightListener(), x, y, width, height, shapeRenderer);
    }
}
