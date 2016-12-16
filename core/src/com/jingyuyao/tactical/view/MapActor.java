package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.HighlightListener;
import com.jingyuyao.tactical.model.GridObject;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * This is responsible for rendering the game object as well as relaying player actions
 * to the game object.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code object.getX()} and {@code object.getY()} after animations
 *
 * @param <T> The game object contained in this actor
 */
class MapActor<T extends GridObject> extends Actor {
    private final T object;
    private final HighlightListener highlightListener;
    private final ShapeRenderer shapeRenderer;
    private Sprite sprite;

    /**
     * Actor's initial position is set to the position of {@code object}.
     * @param object The game object contained by this actor
     * @param size The size of this actor in world units
     * @param highlightListener The listener used to detect highlight changes
     * @param shapeRenderer Renderer used to draw highlights
     */
    MapActor(T object, float size, HighlightListener highlightListener, ShapeRenderer shapeRenderer) {
        this.highlightListener = highlightListener;
        this.object = object;
        this.shapeRenderer = shapeRenderer;
        setBounds(object.getX(), object.getY(), size, size);
        addListener(highlightListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }

        if (highlightListener.isHighlighted()) {
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public T getObject() {
        return object;
    }
}
