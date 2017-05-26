package com.jingyuyao.tactical;

import static org.mockito.Mockito.verifyZeroInteractions;

import com.badlogic.gdx.Application;
import java.util.concurrent.ExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackgroundServiceTest {

  @Mock
  private ExecutorService executorService;
  @Mock
  private Application application;
  @Mock
  private Runnable computation;
  @Mock
  private Runnable resume;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  @Test
  public void submit() {
    BackgroundService backgroundService = new BackgroundService(executorService, application);

    backgroundService.submit(computation, resume);

    InOrder inOrder = Mockito.inOrder(executorService, computation, application);
    verifyZeroInteractions(computation);
    verifyZeroInteractions(resume);
    inOrder.verify(executorService).submit(runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(computation).run();
    inOrder.verify(application).postRunnable(resume);
  }
}