package com.jingyuyao.tactical;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import org.mockito.ArgumentCaptor;

/**
 * Contains utility methods suitable for all tests
 */
public class TestHelpers {

  /**
   * Verifies {@code captor} holds an {@link ObjectEvent} of {@code clazz} at {@code index} and it
   * contains {@code target} as its object.
   */
  public static <T extends ObjectEvent<?>>
  T verifyObjectEvent(ArgumentCaptor<Object> captor, int index, Object target, Class<T> clazz) {
    Object objectAtIndex = null;
    try {
      objectAtIndex = captor.getAllValues().get(index);
    } catch (IndexOutOfBoundsException ex) {
      fail();
    }
    assertThat(objectAtIndex).isNotNull();
    assertThat(objectAtIndex).isInstanceOf(clazz);
    T casted = clazz.cast(objectAtIndex);
    assertThat(casted.getObject()).isSameAs(target);
    return casted;
  }
}
