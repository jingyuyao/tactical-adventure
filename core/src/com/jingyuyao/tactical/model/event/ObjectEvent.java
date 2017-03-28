package com.jingyuyao.tactical.model.event;

/**
 * An event that contains an {@link #object} and some basic utilities. Due to type erasure,
 * subclasses should NOT contain generic parameters since {@link com.google.common.eventbus.EventBus}
 * will not respect type parameters when delivering events. This means {@code A<B>} is treated the
 * same as {@code A<C>} when delivering events. Unlike {@link java.util.EventObject}, subclasses can
 * specify the type of the contained object through generics which makes our life easier.
 *
 * @param <T> the type of contained object.
 */
public class ObjectEvent<T> {

  private final T object;

  ObjectEvent(T object) {
    this.object = object;
  }

  /**
   * Get the object of this event.
   */
  public T getObject() {
    return object;
  }
}
