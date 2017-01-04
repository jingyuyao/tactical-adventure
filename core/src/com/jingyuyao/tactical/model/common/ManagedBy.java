package com.jingyuyao.tactical.model.common;

/**
 * An object that can be initialized and disposed (managed) by listening to a pair of events. Child
 * classes need to mark the two methods with {@link com.google.common.eventbus.Subscribe} so type
 * erasure won't mess with the handlers.
 *
 * @param <I> event type to initialize the object
 * @param <D> event type to dispose the object
 */
public interface ManagedBy<I, D> {
  /**
   * Initialize this {@link ManagedBy} object with {@code data}. Implementation should be able to
   * handle multiple calls. Implementor should mark this with {@link
   * com.google.common.eventbus.Subscribe}.
   *
   * @param data the data to initialize this {@link ManagedBy} object with.
   */
  void initialize(I data);

  /**
   * Dispose this {@link ManagedBy} object with {@code data}. Implementation should be able to
   * handle multiple calls. Implementor should mark this with {@link
   * com.google.common.eventbus.Subscribe}.
   *
   * @param data often an empty class
   */
  void dispose(D data);
}
