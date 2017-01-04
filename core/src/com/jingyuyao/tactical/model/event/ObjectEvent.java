package com.jingyuyao.tactical.model.event;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * An {@link ModelEvent} that contains an {@link #object} and some basic utilities. Due to type
 * erasure, subclasses should NOT contain generics as {@link com.google.common.eventbus.EventBus}
 * will not respect type parameter when delivering events.
 *
 * @param <T> the type of contained object.
 */
public class ObjectEvent<T> implements ModelEvent {
  private final T object;

  public ObjectEvent(T object) {
    this.object = object;
  }

  /** Get the object of this event. */
  public T getObject() {
    return object;
  }

  /** @return whether {@code other} is equal to {@link #object} */
  public boolean matches(Object other) {
    return object.equals(other);
  }

  /** @return a {@link Predicate} that matches to {@link #object} */
  public Predicate<T> getMatchesPredicate() {
    return Predicates.equalTo(object);
  }
}
