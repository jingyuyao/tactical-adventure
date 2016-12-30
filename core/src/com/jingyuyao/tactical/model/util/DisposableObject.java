package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.Disposed;
import com.jingyuyao.tactical.model.event.ResetModel;

/**
 * A {@link Disposable} object that notifies an {@link EventBus} with a {@link Disposed} event.
 */
public class DisposableObject extends EventObject implements Disposable {
    protected DisposableObject(EventBus eventBus) {
        super(eventBus);
    }

    @Subscribe
    public void resetModel(ResetModel resetModel) {
        dispose();
    }

    /**
     * Children should override this method to add more dispose functionality.
     * Make sure super is called.
     */
    @Override
    public void dispose() {
        getEventBus().post(Disposed.create(this));
        getEventBus().unregister(this);
    }
}
