package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.event.Disposed;

/**
 * A {@link Disposable} object that notifies an {@link EventBus} with a {@link Disposed} event.
 */
public class DisposableObject extends EventObject implements Disposable {
    protected DisposableObject(EventBus eventBus) {
        super(eventBus);
    }

    /**
     * Children should override this method to add more dispose functionality.
     * Make sure super is called.
     */
    @Override
    public void dispose() {
        post(Disposed.create(this));
        unregister();
    }
}
