package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.jingyuyao.tactical.GameModule.BackgroundExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provide a safe way of executing logic in a background thread and then resuming in the game's main
 * loop/render thread.
 */
@Singleton
class BackgroundService {

  private final ExecutorService executorService;
  private final Application application;

  @Inject
  BackgroundService(@BackgroundExecutor ExecutorService executorService, Application application) {
    this.executorService = executorService;
    this.application = application;
  }

  /**
   * Submits {@code computation} to a background thread for execution. Upon its completion, {@code
   * resume} is executed in the {@link Application}'s main loop/render thread. {@code computation}
   * should not modify the model or the view. The background thread will only execute one
   * computation at a time. See {@link Executors#newSingleThreadExecutor()} and {@link
   * Application#postRunnable(Runnable)}.
   */
  void submit(final Runnable computation, final Runnable resume) {
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        computation.run();
        application.postRunnable(resume);
      }
    });
  }
}
