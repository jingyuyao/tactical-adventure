package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;

/**
 * An object that has an {@link EventBus} and contains convenience methods for interacting with it.
 * Use {@link DisposableObject} instead of directly subclassing this since {@link DisposableObject}
 * remove itself from {@link EventBus} after it is disposed.
 */
public class EventObject {
    private final EventBus eventBus;
    private boolean registered = false;

    EventObject(EventBus eventBus) {
        this.eventBus = eventBus;
        register();
    }

    /**
     * Register this object to {@link #eventBus} if it has not been registered
     */
    protected void register() {
        if (!registered) {
            eventBus.register(this);
            registered = true;
        }
    }

    /**
     * Unregister this object from {@link #eventBus} if it has been registered
     */
    protected void unregister() {
        if (registered) {
            eventBus.unregister(this);
        }
    }

    /**
     * Delegates to {@link EventBus#post(Object)}.
     */
    protected void post(Object event) {
        eventBus.post(event);
    }

    // TODO: get rid of me
    protected EventBus getEventBus() {
        return eventBus;
    }
}
