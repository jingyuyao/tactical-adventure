package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import javax.inject.Inject;

class HighlightRenderer {
    private final ShapeRenderer shapeRenderer;

    @Inject
    HighlightRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    void draw(Actor actor, Batch batch) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
        shapeRenderer.end();
        batch.begin();
    }
}
