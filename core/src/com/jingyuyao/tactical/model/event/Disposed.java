package com.jingyuyao.tactical.model.event;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Event fired when an object is disposed.
 */
public class Disposed<T> implements ModelEvent {
    private final T object;

    private Disposed(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    /**
     * @return whether {@code other} is equal to the disposed {@link #object}
     */
    public boolean matches(Object other) {
        return object.equals(other);
    }

    /**
     * @return a {@link Predicate} that matches to the disposed {@link #object}
     */
    public Predicate<T> getMatchesPredicate() {
        return Predicates.equalTo(object);
    }

    /**
     * Creates a {@link Disposed} with the given generic typed {@code object}.
     */
    public static <T> Disposed<T> create(T object) {
        return new Disposed<T>(object);
    }
}
