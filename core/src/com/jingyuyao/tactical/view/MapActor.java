package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.HighlightController;
import com.jingyuyao.tactical.model.MapObject;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * This is responsible for rendering the game's objects as well as relaying player actions
 * to the them.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 *
 * @param <T> The game mapObject contained in this actor
 */
class MapActor<T extends MapObject> extends Actor {
    private static final float ACTOR_SIZE = 1f; // world units

    private final T mapObject;
    private final HighlightController highlightController;
    private final ShapeRenderer shapeRenderer;
    private Sprite sprite;

    /**
     * Actor's initial position is set to the position of {@code mapObject}.
     * @param mapObject The game mapObject contained by this actor
     * @param shapeRenderer Renderer used to draw highlights
     * @param highlightController The listener used to detect highlight changes
     */
    MapActor(T mapObject, ShapeRenderer shapeRenderer, HighlightController highlightController) {
        this.highlightController = highlightController;
        this.mapObject = mapObject;
        this.shapeRenderer = shapeRenderer;
        setBounds(mapObject.getX(), mapObject.getY(), ACTOR_SIZE, ACTOR_SIZE);
        addListener(highlightController);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }

        if (mapObject.isHighlighted()) {
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

    public T getMapObject() {
        return mapObject;
    }
}
