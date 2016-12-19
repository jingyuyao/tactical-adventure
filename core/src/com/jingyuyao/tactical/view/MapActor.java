package com.jingyuyao.tactical.view;

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

    MapActor(T object, float size, EventListener... listeners) {
        this.object = object;
        setBounds(object.getX(), object.getY(), size, size);
        object.setUpdateListener(getUpdateListener());
        for (EventListener listener : listeners) {
            addListener(listener);
        }
    }

    protected T getObject() {
        return object;
    }

    protected abstract UpdateListener getUpdateListener();
}
