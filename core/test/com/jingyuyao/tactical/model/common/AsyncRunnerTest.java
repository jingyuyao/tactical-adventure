package com.jingyuyao.tactical.model.common;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncRunnerTest {

  @Mock
  private AsyncRunnable asyncRunnable;
  @Mock
  private AsyncRunnable asyncRunnable2;
  @Mock
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor2;

  private Queue<AsyncRunnable> runnables;
  private AsyncRunner asyncRunner;

  @Before
  public void setUp() {
    runnables = new LinkedList<AsyncRunnable>();
    asyncRunner = new AsyncRunner(runnables);
  }

  @Test
  public void execute_async() {
    asyncRunner.execute(asyncRunnable);
    asyncRunner.execute(asyncRunnable2);

    verify(asyncRunnable).run(runnableCaptor.capture());
    verifyZeroInteractions(asyncRunnable2);
    assertThat(runnables).containsExactly(asyncRunnable2);

    runnableCaptor.getValue().run();
    verify(asyncRunnable2).run(runnableCaptor2.capture());
    assertThat(runnables).isEmpty();

    // Make sure no exceptions are thrown
    runnableCaptor2.getValue().run();
  }

  @Test
  public void execute_runnable() {
    asyncRunner.execute(asyncRunnable);
    asyncRunner.execute(runnable);
    asyncRunner.execute(asyncRunnable2);

    verify(asyncRunnable).run(runnableCaptor.capture());
    verifyZeroInteractions(asyncRunnable2);
    assertThat(runnables).hasSize(2); // the other one is the normal runnable
    assertThat(runnables).contains(asyncRunnable2);

    runnableCaptor.getValue().run();
    verify(runnable).run();
    verify(asyncRunnable2).run(runnableCaptor2.capture());
    assertThat(runnables).isEmpty();

    // Make sure no exceptions are thrown
    runnableCaptor2.getValue().run();
  }
}