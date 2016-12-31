package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;

/**
 * A {@link Disposable} object.
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
        unregister();
    }
}
