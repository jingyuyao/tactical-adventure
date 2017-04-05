package com.jingyuyao.tactical.model.event;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyFutureTest {

  @Mock
  private Runnable runnable;

  private MyFuture future;

  @Before
  public void setUp() {
    future = new MyFuture();
  }

  @Test
  public void isDone() {
    assertThat(future.isDone()).isFalse();

    future.done();

    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void callback_success() {
    future.addCallback(runnable);

    future.done();

    verify(runnable).run();
  }
}