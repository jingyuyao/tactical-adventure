package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

import java.util.Observable;
import java.util.Observer;

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
            notifyObservers(new AnimationChange(isAnimating()));
        }
    }

    /**
     * Decrements the number of current animations by one. Notify observers if animation counter was one.
     */
    public void finishOneAnimation() {
        Preconditions.checkState(animations > 0, "Oh boy, this bug is gonna be hard to fix");
        if (animations-- == 1) {
            setChanged();
            notifyObservers(new AnimationChange(isAnimating()));
        }
    }

    /**
     * Run {@code runnable} immediately if there are no animation. If there are animations, wait until they
     * finish then run {@code runnable} once.
     */
    public void runOnceWhenNotAnimating(final Runnable runnable) {
        if (!isAnimating()) {
            runnable.run();
        } else {
            addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    if (!isAnimating()) {
                        runnable.run();
                        deleteObserver(this);
                    }
                }
            });
        }
    }

    public static class AnimationChange {
        private final boolean animating;

        private AnimationChange(boolean animating) {
            this.animating = animating;
        }

        public boolean isAnimating() {
            return animating;
        }
    }
}
