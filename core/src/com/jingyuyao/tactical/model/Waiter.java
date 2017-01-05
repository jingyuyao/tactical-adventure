package com.jingyuyao.tactical.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.ModelEvent;
import com.jingyuyao.tactical.model.event.NewMap;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Queue;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A semaphore like object that posts change in its state.
 */
@Singleton
public class Waiter extends EventBusObject implements ManagedBy<NewMap, ClearMap> {

  private final Queue<Runnable> runnableQueue;
  private int waits = 0;

  @Inject
  public Waiter(EventBus eventBus, @BackingWaiterQueue Queue<Runnable> runnableQueue) {
    super(eventBus);
    this.runnableQueue = runnableQueue;
    register();
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    // does nothing
  }

  @Subscribe
  @Override
  public void dispose(ClearMap clearMap) {
    runnableQueue.clear();
    waits = 0;
  }

  @Subscribe
  public void changed(Changed changed) {
    if (changed.canProceed()) {
      while (!runnableQueue.isEmpty()) {
        runnableQueue.poll().run();
      }
    }
  }

  public boolean canProceed() {
    return waits == 0;
  }

  /**
   * Increments the number of current waits by one. Posts an event if wait count was zero.
   */
  public void waitOne() {
    if (waits++ == 0) {
      post(new Changed(canProceed()));
    }
  }

  /**
   * Decrements the number of current waits by one. Posts an event if wait count was one.
   */
  public void finishOne() {
    Preconditions.checkState(waits > 0, "Oh boy, this bug is gonna be hard to fix");
    if (waits-- == 1) {
      post(new Changed(canProceed()));
    }
  }

  /**
   * Run {@code runnable} immediately if {@link #canProceed()} is true else wait until {@link
   * #canProceed()} becomes true. The order of {@code runnable}s are maintained
   */
  public void runOnce(final Runnable runnable) {
    if (canProceed()) {
      runnable.run();
    } else {
      runnableQueue.add(runnable);
    }
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingWaiterQueue {

  }

  public static class Changed implements ModelEvent {

    private final boolean proceed;

    private Changed(boolean proceed) {
      this.proceed = proceed;
    }

    public boolean canProceed() {
      return proceed;
    }
  }
}
