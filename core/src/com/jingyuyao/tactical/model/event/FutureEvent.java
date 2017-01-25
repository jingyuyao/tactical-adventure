package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.SettableFuture;

/**
 * An event that must be completed externally (usually by the view).
 */
public class FutureEvent<T> extends ObjectEvent<T> {

  private final SettableFuture<Void> future;

  /**
   * See {@link ObjectEvent#ObjectEvent(Object)} to why this is protected.
   */
  protected FutureEvent(T object, SettableFuture<Void> future) {
    super(object);
    this.future = future;
  }

  /**
   * Signals this event has finished being processed and model code can continue.
   */
  public void done() {
    future.set(null);
  }
}
