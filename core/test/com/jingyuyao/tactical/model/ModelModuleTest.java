package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.MapState;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelModuleTest {

  @Bind
  @Mock
  private EventBus eventBus;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  @Inject
  private Model model;
  @Inject
  private Characters characters;
  @Inject
  private Terrains terrains;
  @Inject
  private MapState mapState;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new ModelModule()).injectMembers(this);
  }

  @Test
  public void has_correct_subscribers() {
    model.registerEventSubscribers();

    verify(eventBus, times(3)).register(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).containsExactly(characters, terrains, mapState);
  }
}
