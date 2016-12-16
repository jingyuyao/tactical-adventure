package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.controller.HighlightListener;

import javax.inject.Inject;

class Highlighter  {
    private final HighlightListener highlightListener;
    private final ShapeRenderer shapeRenderer;
    private Actor actor;

    @Inject
    Highlighter(HighlightListener highlightListener, ShapeRenderer shapeRenderer) {
        this.highlightListener = highlightListener;
        this.shapeRenderer = shapeRenderer;
    }

    /**
     * Sets the {@link Actor} this highlighter listens to. Must be called before draw().
     *
     * This method should only be called once.
     *
     * @param actor The actor to bind this highlighter to
     */
    void setActor(Actor actor) {
        Preconditions.checkArgument(this.actor == null, "setActor should only be called once.");
        actor.addListener(highlightListener);
        this.actor = actor;
    }

    /**
     * Draws a box around the actor bounded by {@link #setActor(Actor)} if it is highlighted.
     *
     * Highlighted = hovered over or touched
     *
     * @param batch The batch to draw the highlight with
     */
    void draw(Batch batch) {
        if (highlightListener.isHighlighted()) {
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
}
