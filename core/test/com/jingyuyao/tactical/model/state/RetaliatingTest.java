package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.AsyncRunnable;
import com.jingyuyao.tactical.model.common.AsyncRunner;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetaliatingTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Characters characters;
  @Mock
  private AsyncRunner asyncRunner;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private AsyncRunnable retaliation;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private ImmutableList<Character> characterList;
  private Retaliating retaliating;

  @Before
  public void setUp() {
    characterList = ImmutableList.of(player, enemy);
    retaliating = new Retaliating(eventBus, mapState, stateFactory, characters, asyncRunner);
  }

  @Test
  public void selects() {
    retaliating.select(player);
    retaliating.select(enemy);
    retaliating.select(terrain);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void actions() {
    assertThat(retaliating.getActions()).isEmpty();
  }

  @Test
  public void enter() {
    when(characters.iterator()).thenReturn(characterList.iterator());
    when(enemy.getRetaliation()).thenReturn(retaliation);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    retaliating.enter();

    InOrder inOrder = Mockito.inOrder(asyncRunner);
    inOrder.verify(asyncRunner).execute(retaliation);
    inOrder.verify(asyncRunner).execute(runnableCaptor.capture());
    verifyZeroInteractions(mapState);

    runnableCaptor.getValue().run();
    verify(mapState).newStack(waiting);
  }
}