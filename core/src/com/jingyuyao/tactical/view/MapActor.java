package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.UpdateListener;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
public abstract class MapActor<T extends MapObject> extends Actor {
    private final T object;
    private Sprite sprite;

    MapActor(T object, float size, EventListener... listeners) {
        this(object, size, null, listeners);
    }

    MapActor(T object, float size, Sprite sprite, EventListener... listeners) {
        this.object = object;
        this.sprite = sprite;
        setBounds(object.getX(), object.getY(), size, size);
        object.setUpdateListener(getUpdateListener());
        for (EventListener listener : listeners) {
            addListener(listener);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
    }

    void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    protected T getObject() {
        return object;
    }

    protected abstract UpdateListener getUpdateListener();
}
