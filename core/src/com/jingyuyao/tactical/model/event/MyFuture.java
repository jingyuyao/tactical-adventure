package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Wraps Guava's {@link SettableFuture}
 */
public class MyFuture {

  private final SettableFuture<Void> future;

  public MyFuture() {
    this.future = SettableFuture.create();
  }

  public boolean isDone() {
    return future.isDone();
  }

  public void done() {
    future.set(null);
  }

  public void addCallback(final Runnable onSuccess) {
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
