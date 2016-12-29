package com.jingyuyao.tactical.model.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Event fired when an object is disposed.
 */
// TODO: are there any generic and type safe way of doing class checking
public class Disposed {
    private final Object object;

    Disposed(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    /**
     * @return whether the containing object is of a certain {@code clazz}.
     */
    public boolean isOfClass(Class clazz) {
        return clazz.isInstance(object);
    }

    /**
     * @return {@link #object} as a certain {@code clazz}
     */
    public <T> T getObjectAs(Class<T> clazz) {
        Preconditions.checkArgument(isOfClass(clazz));
        return clazz.cast(object);
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
    public Predicate<Object> getMatchesPredicate() {
        return Predicates.equalTo(object);
    }
}
