package com.jingyuyao.tactical.model.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Singleton;

public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Algorithms.class);
  }

  @Provides
  @Singleton
  Queue<AsyncRunnable> provideAsyncRunnables() {
    return new LinkedList<AsyncRunnable>();
  }
}
