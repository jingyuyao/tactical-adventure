package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.object.GridObject;

/**
 * An {@link Actor} on a {@link Map}.
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
    private final Highlighter highlighter;
    private final T object;
    private Sprite sprite;

    /**
     * Actor's initial position is set to the position of {@code object}.
     *
     * @param object The game object contained by this actor
     * @param size The size of this actor in world units
     * @param highlighter The renderer used to draw the border around the actor when its highlighted
     */
    MapActor(T object, float size, Highlighter highlighter) {
        this.highlighter = highlighter;
        this.object = object;
        setBounds(object.getX(), object.getY(), size, size);
        highlighter.setActor(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }

        highlighter.draw(batch);
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
