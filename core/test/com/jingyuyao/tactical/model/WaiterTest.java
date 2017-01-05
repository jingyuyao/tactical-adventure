package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.event.ClearMap;
import java.util.Queue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaiterTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Queue<Runnable> runnableQueue;
  @Mock
  private ClearMap clearMap;
  @Mock
  private Waiter.Changed changed;
  @Mock
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Waiter waiter;

  @Before
  public void setUp() {
    waiter = new Waiter(eventBus, runnableQueue);
    verify(eventBus).register(waiter);
  }

  @Test
  public void dispose() {
    waiter.dispose(clearMap);

    verify(runnableQueue).clear();
  }

  @Test
  public void changed_cannot_proceed() {
    when(changed.canProceed()).thenReturn(false);

    waiter.changed(changed);

    verifyZeroInteractions(runnableQueue);
  }

  @Test
  public void changed_can_proceed() {
    when(changed.canProceed()).thenReturn(true);
    when(runnableQueue.isEmpty()).thenReturn(false).thenReturn(true);
    when(runnableQueue.poll()).thenReturn(runnable);

    waiter.changed(changed);

    verify(runnableQueue).poll();
    verify(runnable).run();
  }

  @Test
  public void canProceed() {
    assertThat(waiter.canProceed()).isTrue();
    waiter.waitOne();
    assertThat(waiter.canProceed()).isFalse();
    waiter.finishOne();
    assertThat(waiter.canProceed()).isTrue();
  }

  @Test
  public void waitOne() {
    waiter.waitOne();

    verify(eventBus).post(argumentCaptor.capture());
    verifyChanged(argumentCaptor.getValue(), false);

    waiter.waitOne();

    verifyNoMoreInteractions(eventBus);
  }

  @Test(expected = IllegalStateException.class)
  public void finishOne_exception() {
    waiter.finishOne();
  }

  @Test
  public void finishOne() {
    waiter.waitOne();
    waiter.waitOne();

    reset(eventBus);

    waiter.finishOne();
    verifyZeroInteractions(eventBus);

    reset(eventBus);

    waiter.finishOne();
    verify(eventBus).post(argumentCaptor.capture());
    verifyChanged(argumentCaptor.getValue(), true);
  }

  @Test
  public void runOnce_can_proceed() {
    waiter.runOnce(runnable);

    verify(runnable).run();
  }

  @Test
  public void runOnce_cannot_proceed() {
    waiter.waitOne();

    waiter.runOnce(runnable);

    verify(runnableQueue).add(runnable);
    verifyZeroInteractions(runnable);
  }

  private void verifyChanged(Object event, boolean canProceed) {
    Waiter.Changed changed = TestHelpers.isInstanceOf(event, Waiter.Changed.class);
    assertThat(changed.canProceed()).isEqualTo(canProceed);
  }
}
