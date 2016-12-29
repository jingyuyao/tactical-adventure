package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

/**
 * A semaphore like object that notifies its observers change in its state.
 */
public class Waiter extends Observable {
    private final EventBus eventBus;
    private final Queue<Runnable> runnables = new LinkedList<Runnable>();
    private boolean hasObserver = false;
    private int waits = 0;

    public Waiter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public boolean isWaiting() {
        return waits != 0;
    }

    /**
     * Increments the number of current waits by one. Notify observers if wait count was zero.
     */
    public void waitOne() {
        if (waits++ == 0) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Decrements the number of current waits by one. Notify observers if wait count was one.
     */
    public void finishOne() {
        Preconditions.checkState(waits > 0, "Oh boy, this bug is gonna be hard to fix");
        if (waits-- == 1) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Run {@code runnable} immediately if {@link #isWaiting()} is false else wait until {@link #isWaiting()}
     * becomes true. The {@code runnable}s are executed in the order at which {@link #runOnce(Runnable)} is called.
     */
    public void runOnce(final Runnable runnable) {
        if (!isWaiting()) {
            runnable.run();
        } else {
            runnables.add(runnable);
            // The order at which the observers are notified is unspecified which means we need to keep
            // track of the order ourselves. This observer will run all queued up runnables when
            // we are not waiting then remove itself. We limit the number of observers to one.
            if (!hasObserver) {
                hasObserver = true;
                addObserver(new Observer() {
                    @Override
                    public void update(Observable observable, Object o) {
                        if (!isWaiting()) {
                            while (!runnables.isEmpty()) {
                                runnables.poll().run();
                            }
                            deleteObserver(this);
                            hasObserver = false;
                        }
                    }
                });
            }
        }
    }
}
