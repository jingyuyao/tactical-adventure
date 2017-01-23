package com.jingyuyao.tactical.model;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

  @Mock
  private EventBus eventBus;

  @Test
  public void register_event_subscribers_all_annotated() {
    EventSubscriber annotated = new SingletonAnnotated();
    Model model = new Model(eventBus, ImmutableSet.of(annotated));

    model.registerEventSubscribers();

    verify(eventBus).register(annotated);
  }

  @Test
  public void register_event_subscribers_has_not_annotated() {
    EventSubscriber annotated = new SingletonAnnotated();
    EventSubscriber notAnnotated = new NotSingletonAnnotated();
    Model model = new Model(eventBus, ImmutableSet.of(annotated, notAnnotated));

    try {
      model.registerEventSubscribers();
      fail();
    } catch (IllegalStateException e) {
      verifyZeroInteractions(eventBus);
    }
  }

  @Singleton
  private static class SingletonAnnotated implements EventSubscriber {

  }

  private static class NotSingletonAnnotated implements EventSubscriber {

  }
}