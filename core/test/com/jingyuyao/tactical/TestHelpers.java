package com.jingyuyao.tactical;

import static com.google.common.truth.Truth.assertThat;

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
}
