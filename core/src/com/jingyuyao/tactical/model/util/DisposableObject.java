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

    @Override
    public void dispose() {
        disposed();
        getEventBus().post(Disposed.create(this));
        getEventBus().unregister(this);
    }

    /**
     * Called before {@link Disposed} event is fired.
     * Children should override this to add clean up code.
     */
    protected void disposed() {}
}
