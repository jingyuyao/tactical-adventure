package com.jingyuyao.tactical.model.common;

/**
 * For objects that needs to be cleaned up when being removed from the model. e.g. anything that
 * registers itself to the {@link com.google.common.eventbus.EventBus}.
 */
public interface Disposable {
  /**
   * Run any necessary clean up code to remove this object from the model or reset it to a clean
   * state. Invariant: do not use {@link com.google.common.eventbus.EventBus} while disposing.
   */
  void dispose();
}
