package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.util.ModelEvent;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A semaphore like object that posts change in its state.
 */
public class Waiter {
    private final EventBus eventBus;
    private final Queue<Runnable> runnables;
    private int waits = 0;

    public Waiter(EventBus eventBus) {
        this.eventBus = eventBus;
        runnables = new LinkedList<Runnable>();
        // Waiter listens to its own event.
        eventBus.register(this);
    }

    public boolean isWaiting() {
        return waits != 0;
    }

    /**
     * Increments the number of current waits by one. Posts an event if wait count was zero.
     */
    public void waitOne() {
        if (waits++ == 0) {
            eventBus.post(new WaitChange(isWaiting()));
        }
    }

    /**
     * Decrements the number of current waits by one. Posts an event if wait count was one.
     */
    public void finishOne() {
        Preconditions.checkState(waits > 0, "Oh boy, this bug is gonna be hard to fix");
        if (waits-- == 1) {
            eventBus.post(new WaitChange(isWaiting()));
        }
    }

    /**
     * Run {@code runnable} immediately if {@link #isWaiting()} is false else wait until {@link #isWaiting()}
     * becomes true. The order of {@code runnable}s are maintained
     */
    public void runOnce(final Runnable runnable) {
        if (!isWaiting()) {
            runnable.run();
        } else {
            runnables.add(runnable);
        }
    }

    @Subscribe
    public void waitChange(WaitChange waitChange) {
        if (!waitChange.isWaiting()) {
            while (!runnables.isEmpty()) {
                runnables.poll().run();
            }
        }
    }

    public static class WaitChange implements ModelEvent {
        private final boolean waiting;

        private WaitChange(boolean waiting) {
            this.waiting = waiting;
        }

        public boolean isWaiting() {
            return waiting;
        }
    }
}
