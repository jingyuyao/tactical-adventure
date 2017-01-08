package com.jingyuyao.tactical.model.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Singleton;

public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Waiter.class);
  }

  @Provides
  @Singleton
  @Waiter.BackingWaiterQueue
  Queue<Runnable> provideRunnableQueue() {
    return new LinkedList<Runnable>();
  }
}
