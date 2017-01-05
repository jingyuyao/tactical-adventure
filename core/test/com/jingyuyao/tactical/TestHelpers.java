package com.jingyuyao.tactical;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

/**
 * Contains utility methods suitable for all tests
 */
public class TestHelpers {

  /**
   * Assert {@code object} is an instance of {@code clazz} then return the casted instance.
   */
  public static <T> T isInstanceOf(Object object, Class<T> clazz) {
    assertThat(object).isInstanceOf(clazz);
    return clazz.cast(object);
  }

  /**
   * Verifies that {@code objectToTest} handles {@code events}.
   *
   * @param objectToTest the object to test for {@link Subscribe} methods
   * @param events the events {@code objectToTest} should handle
   */
  public static void verifyNoDeadEvents(Object objectToTest, Object... events) {
    EventBus eventBus = new EventBus("test");
    eventBus.register(new VerifyNoDeadEvent());
    eventBus.register(objectToTest);
    for (Object event : events) {
      eventBus.post(event);
    }
  }

  /**
   * A class that fails when detecting a {@link DeadEvent}.
   */
  private static class VerifyNoDeadEvent {

    @Subscribe
    public void deadEvent(DeadEvent deadEvent) {
      fail();
    }
  }
}
