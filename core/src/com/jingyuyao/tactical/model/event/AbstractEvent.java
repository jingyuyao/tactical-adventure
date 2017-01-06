package com.jingyuyao.tactical.model.event;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * An {@link ModelEvent} that contains an {@link #object} and some basic utilities. Due to type
 * erasure, subclasses should NOT contain generic parameters since {@link
 * com.google.common.eventbus.EventBus} will not respect type parameters when delivering events.
 * This means {@code A<B>} is treated the same as {@code A<C>} when delivering events.
 * Unlike {@link java.util.EventObject}, subclasses can specify the type of the contained
 * object through generics which makes our life easier.
 *
 * @param <T> the type of contained object.
 */
public abstract class AbstractEvent<T> implements ModelEvent {

  private final T object;

  public AbstractEvent(T object) {
    this.object = object;
  }

  /**
   * Get the object of this event.
   */
  public T getObject() {
    return object;
  }

  /**
   * @return whether {@code other} is equal to {@link #object}
   */
  public boolean matches(Object other) {
    return object.equals(other);
  }

  /**
   * @return a {@link Predicate} that matches to {@link #object}
   */
  public Predicate<T> getMatchesPredicate() {
    return Predicates.equalTo(object);
  }
}
