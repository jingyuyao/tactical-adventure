package com.jingyuyao.tactical.model.common;

/**
 * A {@link Runnable} that is completed asynchronously.
 * <br>
 * Compared to something like {@link java.util.concurrent.RunnableFuture}, this runnable is not
 * intended to be executed in a separate thread. Instead, it runs in the thread in which it was
 * called in a non-blocking manner by using callbacks to signal resume execution. This interface
 * can also be seen as a more lightweight version of {@code CompletableFuture} introduced in Java 8.
 * Unlike {@code CompletableFuture}, this interface does not produce any result. It simply notifies
 * the caller about when it is finished running.
 */
public interface AsyncRunnable {

  /**
   * Similar to JavaScripts promise interface, this method takes a "function" to be called
   * when the run method has finished running.
   *
   * @param done to be executed when this {@link AsyncRunnable} has finished.
   */
  void run(Runnable done);
}
