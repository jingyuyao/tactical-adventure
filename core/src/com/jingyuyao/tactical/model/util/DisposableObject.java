package com.jingyuyao.tactical.model.util;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * A {@link Disposable} object that notifies an {@link EventBus} with a {@link Disposed} event.
 */
public class DisposableObject extends EventObject implements Disposable {
    public DisposableObject(EventBus eventBus) {
        super(eventBus);
        getEventBus().register(this);
    }

    @Subscribe
    public void resetModel(ResetModel resetModel) {
        dispose();
    }

    @Override
    public void dispose() {
        disposed();
        getEventBus().post(Disposed.create(this));
    }

    /**
     * Called before {@link Disposed} event is fired.
     * Children should override this to add clean up code.
     */
    protected void disposed() {}
}
