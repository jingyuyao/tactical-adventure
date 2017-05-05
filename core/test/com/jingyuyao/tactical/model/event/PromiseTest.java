package com.jingyuyao.tactical.model.event;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PromiseTest {

  @Mock
  private Runnable runnable;

  private Promise promise;

  @Before
  public void setUp() {
    promise = new Promise();
  }

  @Test
  public void immediate() {
    assertThat(Promise.immediate().isDone()).isTrue();
  }

  @Test
  public void isDone() {
    assertThat(promise.isDone()).isFalse();

    promise.complete();

    assertThat(promise.isDone()).isTrue();
  }

  @Test
  public void callback_success() {
    promise.done(runnable);

    promise.complete();

    verify(runnable).run();
  }
}