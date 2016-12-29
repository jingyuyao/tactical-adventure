package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;

/**
 * An object that has an {@link EventBus}
 */
public class EventObject {
    private final EventBus eventBus;

    public EventObject(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    protected EventBus getEventBus() {
        return eventBus;
    }
}
