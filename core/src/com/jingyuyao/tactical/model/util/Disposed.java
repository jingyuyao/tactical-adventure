package com.jingyuyao.tactical.model.util;

/**
 * Event fired when an object is disposed.
 */
public class Disposed {
    private final Object object;

    Disposed(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
