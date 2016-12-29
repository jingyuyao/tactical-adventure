package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;

/**
 * An object that is linked to an {@link EventBus}.
 */
public class EventObject {
    private final EventBus eventBus;

    public EventObject(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    protected EventBus getEventBus() {
        return eventBus;
    }
}
