package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.ModelEvent;
import com.jingyuyao.tactical.model.event.NewMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Queue;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A semaphore like object that posts change in its state.
 */
@Singleton
public class Waiter extends EventBusObject implements ManagedBy<NewMap, ClearMap> {
    private final Queue<Runnable> runnables;
    private int waits = 0;

    @Inject
    public Waiter(EventBus eventBus, @InitialWaiterQueue Queue<Runnable> runnables) {
        super(eventBus);
        this.runnables = runnables;
        register();
    }

    @Subscribe
    @Override
    public void initialize(NewMap data) {
        // does nothing
    }

    @Subscribe
    @Override
    public void dispose(ClearMap clearMap) {
        runnables.clear();
        waits = 0;
    }

    @Subscribe
    public void changed(Changed changed) {
        if (!changed.isWaiting()) {
            while (!runnables.isEmpty()) {
                runnables.poll().run();
            }
        }
    }

    public boolean isWaiting() {
        return waits != 0;
    }

    /**
     * Increments the number of current waits by one. Posts an event if wait count was zero.
     */
    public void waitOne() {
        if (waits++ == 0) {
            post(new Changed(isWaiting()));
        }
    }

    /**
     * Decrements the number of current waits by one. Posts an event if wait count was one.
     */
    public void finishOne() {
        Preconditions.checkState(waits > 0, "Oh boy, this bug is gonna be hard to fix");
        if (waits-- == 1) {
            post(new Changed(isWaiting()));
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

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialWaiterQueue {}

    public static class Changed implements ModelEvent {
        private final boolean waiting;

        private Changed(boolean waiting) {
            this.waiting = waiting;
        }

        public boolean isWaiting() {
            return waiting;
        }
    }
}
