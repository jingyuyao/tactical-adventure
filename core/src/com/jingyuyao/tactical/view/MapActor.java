package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.MapObject;

import java.util.Observable;
import java.util.Observer;

/**
 * An {@link Actor} on a {@link MapView}.
 *
 * Invariants:
 * - getX() and getY() should ultimately match {@code mapObject.getX()} and {@code mapObject.getY()} after animations
 */
abstract class MapActor<T extends MapObject> extends Actor implements Observer {
    private final AnimationCounter animationCounter;

    /**
     * @param object This will be the first argument in {@link #update(Observable, Object)}
     */
    MapActor(T object, float size, AnimationCounter animationCounter, EventListener listener) {
        this.animationCounter = animationCounter;
        setBounds(object.getCoordinate().getX(), object.getCoordinate().getY(), size, size);
        addListener(listener);
        object.addObserver(this);
    }

    AnimationCounter getAnimationCounter() {
        return animationCounter;
    }
}
