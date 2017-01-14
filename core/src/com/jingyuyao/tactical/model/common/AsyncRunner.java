package com.jingyuyao.tactical.model.common;

import com.google.inject.Singleton;
import java.util.Queue;
import javax.inject.Inject;

/**
 * Executes {@link AsyncRunnable} sequentially.
 */
@Singleton
public class AsyncRunner {

  private final Queue<AsyncRunnable> runnables;
  private boolean running = false;

  @Inject
  AsyncRunner(Queue<AsyncRunnable> runnables) {
    this.runnables = runnables;
  }

  /**
   * Executes {@code asyncRunnable} immediately if there are no other {@link AsyncRunnable} running,
   * else adds it to a queue to be executed after the previous {@link AsyncRunnable} have finished.
   */
  public void execute(AsyncRunnable asyncRunnable) {
    runnables.add(asyncRunnable);
    if (!running) {
      running = true;
      executeNext();
    }
  }

  /**
   * Executes a normal {@link Runnable} as if it is a {@link AsyncRunnable}.
   */
  public void execute(final Runnable runnable) {
    execute(new AsyncRunnable() {
      @Override
      public void run(Runnable done) {
        runnable.run();
        // Calling done.run() instead of done() directly for more portability?
        done.run();
      }
    });
  }

  private void executeNext() {
    runnables.poll().run(new Runnable() {
      @Override
      public void run() {
        done();
      }
    });
  }

  private void done() {
    if (runnables.isEmpty()) {
      running = false;
    } else {
      executeNext();
    }
  }
}
