package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

import java.util.Observable;
import java.util.Observer;

/**
 * A semaphore like object that notifies its observers change in its state.
 */
public class Waiter extends Observable {
    private int waits = 0;

    public boolean isWaiting() {
        return waits != 0;
    }

    /**
     * Increments the number of current waits by one. Notify observers if wait count was zero.
     */
    public void waitOne() {
        if (waits++ == 0) {
            setChanged();
            notifyObservers(new Change(isWaiting()));
        }
    }

    /**
     * Decrements the number of current waits by one. Notify observers if wait count was one.
     */
    public void finishOne() {
        Preconditions.checkState(waits > 0, "Oh boy, this bug is gonna be hard to fix");
        if (waits-- == 1) {
            setChanged();
            notifyObservers(new Change(isWaiting()));
        }
    }

    /**
     * Run {@code runnable} immediately if there are no animation. If there are waits, wait until they
     * finish then run {@code runnable} once.
     */
    public void runOnceWhenNotWaiting(final Runnable runnable) {
        if (!isWaiting()) {
            runnable.run();
        } else {
            addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    if (!isWaiting()) {
                        runnable.run();
                        deleteObserver(this);
                    }
                }
            });
        }
    }

    public static class Change {
        private final boolean waiting;

        private Change(boolean waiting) {
            this.waiting = waiting;
        }

        public boolean isWaiting() {
            return waiting;
        }
    }
}
