package com.jingyuyao.tactical.model.common;

import com.google.common.eventbus.EventBus;

/**
 * An object that has an {@link EventBus} and contains convenience methods for interacting with it.
 */
public class EventBusObject {
    private final EventBus eventBus;
    private boolean registered = false;

    protected EventBusObject(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Register this object to {@link #eventBus} if it has not been registered.
     * Safe to be called multiple times without any performance hit. Each class
     * with {@link com.google.common.eventbus.Subscribe} annotation should call
     * this just in case the parent class doesn't call it.
     */
    protected void register() {
        if (!registered) {
            eventBus.register(this);
            registered = true;
        }
    }

    /**
     * Unregister this object from {@link #eventBus} if it has been registered.
     */
    protected void unregister() {
        if (registered) {
            eventBus.unregister(this);
            registered = false;
        }
    }

    /**
     * Delegates to {@link EventBus#post(Object)}.
     */
    protected void post(Object event) {
        eventBus.post(event);
    }
}
