package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;

/**
 * An object that is linked to an {@link EventBus}.
 * Use {@link DisposableObject} instead of directly subclassing this since {@link DisposableObject}
 * remove itself from {@link EventBus} after it is disposed.
 */
public class EventObject {
    private final EventBus eventBus;

    EventObject(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    protected EventBus getEventBus() {
        return eventBus;
    }
}
