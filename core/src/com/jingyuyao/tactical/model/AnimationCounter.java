package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

import java.util.Observable;

/**
 * An object that counts the current number of animations and alerts observers when total animation state changes.
 */
public class AnimationCounter extends Observable {
    private int animations = 0;

    public boolean isAnimating() {
        return animations != 0;
    }

    /**
     * Increments the number of current animations by one. Notify observers if animation count was zero.
     */
    public void startOneAnimation() {
        if (animations++ == 0) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Decrements the number of current animations by one. Notify observers if animation counter was one.
     */
    public void finishOneAnimation() {
        Preconditions.checkState(animations > 0, "Oh boy, this bug is gonna be hard to fix");
        if (animations-- == 1) {
            setChanged();
            notifyObservers();
        }
    }
}
