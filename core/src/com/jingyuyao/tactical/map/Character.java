package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class Character extends MapActor {
    private final Terrain terrain;
    private final ShapeRenderer shapeRenderer;

    Character(Map map, HighlightRenderer highlightRenderer, HighlightListener highlightListener,
              float x, float y, float width, float height, ShapeRenderer shapeRenderer) {
        super(map, highlightRenderer, highlightListener, x, y, width, height);
        this.shapeRenderer = shapeRenderer;
        this.terrain = map.getTerrain(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}
