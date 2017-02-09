package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Transition transition;

  @Before
  public void setUp() {
    transition = new Transition(eventBus, mapState);
  }

  @Test
  public void enter() {
    transition.enter();

    verify(eventBus).post(transition);
  }

  @Test
  public void exit() {
    transition.exit();

    verify(mapState).popLast();
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, transition, ExitState.class);
  }

  @Test
  public void selects() {
    transition.select(player);
    transition.select(enemy);
    transition.select(terrain);

    verifyZeroInteractions(mapState);
    verifyZeroInteractions(player);
    verifyZeroInteractions(enemy);
    verifyZeroInteractions(terrain);
  }

  @Test
  public void actions() {
    assertThat(transition.getActions()).isEmpty();
  }
}