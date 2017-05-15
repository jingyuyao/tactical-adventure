package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.eventbus.Subscribe;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelModuleTest {

  @Inject
  private ModelBus modelBus;

  @Test
  public void can_create_module() {
    Guice.createInjector(new ModelModule()).injectMembers(this);
  }

  @Test
  public void register_good_listener() {
    Injector injector = Guice.createInjector(new ModelModule());
    GoodListener goodListener = injector.getInstance(GoodListener.class);
    TestEvent event = new TestEvent();

    injector.getInstance(ModelBus.class).post(event);

    assertThat(goodListener.event).isSameAs(event);
  }

  @Test(expected = ConfigurationException.class)
  public void fail_bad_listener() {
    Injector injector = Guice.createInjector(new ModelModule());
    injector.getInstance(BadListener.class);
  }

  @Singleton
  @ModelBusListener
  private static class GoodListener {

    private TestEvent event;

    @Subscribe
    void testEvent(TestEvent event) {
      this.event = event;
    }
  }

  @ModelBusListener
  private static class BadListener {

  }

  private static class TestEvent {

  }
}
