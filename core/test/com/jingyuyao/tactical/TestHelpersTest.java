package com.jingyuyao.tactical;

import com.google.common.eventbus.Subscribe;
import org.junit.Test;

/**
 * Heh.
 */
public class TestHelpersTest {
    @Test
    public void verifyNoDeadEvents_pass() {
        TestHelpers.verifyNoDeadEvents(this, new SubscribedEvent());
    }

    @Test(expected = AssertionError.class)
    public void verifyNoDeadEvents_fail() {
        TestHelpers.verifyNoDeadEvents(this, new NotSubscribedEvent());
    }

    @Subscribe
    public void subscribe(SubscribedEvent unsed) {
        // Hi!
    }

    private static class SubscribedEvent {}

    private static class NotSubscribedEvent {}
}
