package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Wraps Guava's {@link SettableFuture}
 */
public class Promise {

  private final SettableFuture<Void> future;

  public Promise() {
    this.future = SettableFuture.create();
  }

  public Promise(Runnable... callbacks) {
    this();
    for (Runnable callback : callbacks) {
      done(callback);
    }
  }

  public static Promise immediate() {
    Promise promise = new Promise();
    promise.complete();
    return promise;
  }

  public boolean isDone() {
    return future.isDone();
  }

  public void complete() {
    future.set(null);
  }

  /**
   * Add a callback to run when this promise completes
   */
  public void done(final Runnable onSuccess) {
    Futures.addCallback(future, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        onSuccess.run();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }
}
