package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelBusTest {

  private EventBus eventBus;
  private ModelBus modelBus;
  private PrivateEvent privateEvent;
  private DeadEvent deadEvent;

  @Before
  public void setUp() {
    eventBus = new EventBus();
    modelBus = new ModelBus(eventBus);
    privateEvent = null;
    deadEvent = null;
  }

  @Test
  public void identifier() {
    assertThat(modelBus.identifier()).isEqualTo(eventBus.identifier());
  }

  @Test
  public void to_string() {
    assertThat(modelBus.toString()).isEqualTo(eventBus.toString());
  }

  @Test
  public void post_events() {
    modelBus.register(this);
    PrivateEvent event = new PrivateEvent();

    modelBus.post(event);

    assertThat(privateEvent).isSameAs(event);
    assertThat(deadEvent).isNull();
  }

  @Test
  public void dead_events() {
    modelBus.register(this);
    SadEvent event = new SadEvent();

    modelBus.post(event);

    assertThat(privateEvent).isNull();
    assertThat(deadEvent).isNotNull();
    assertThat(deadEvent.getEvent()).isSameAs(event);
  }

  @Test
  public void unregister() {
    modelBus.register(this);
    modelBus.unregister(this);

    modelBus.post(new PrivateEvent());

    assertThat(privateEvent).isNull();
    assertThat(deadEvent).isNull();
  }

  @Subscribe
  void privateEvent(PrivateEvent event) {
    privateEvent = event;
  }

  @Subscribe
  void deadEvent(DeadEvent event) {
    deadEvent = event;
  }

  private static class PrivateEvent {

  }

  private static class SadEvent {

  }
}