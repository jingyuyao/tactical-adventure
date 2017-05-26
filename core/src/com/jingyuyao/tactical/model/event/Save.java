package com.jingyuyao.tactical.model.event;

/**
 * Emitted by model when it wishes to be saved.
 */
public class Save {

  private final Promise promise;

  public Save(Promise promise) {
    this.promise = promise;
  }

  public void complete() {
    promise.complete();
  }
}
