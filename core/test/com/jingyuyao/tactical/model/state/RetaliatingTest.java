package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.terrain.Terrain;
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
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
  @Mock
  private Characters characters;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Enemy enemy2;
  @Mock
  private Terrain terrain;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ListenableFuture<Void> retaliation;
  private ListenableFuture<Void> retaliation2;
  private Retaliating retaliating;

  @Before
  public void setUp() {
    retaliation = Futures.immediateFuture(null);
    retaliation2 = Futures.immediateFuture(null);
    retaliating = new Retaliating(eventBus, mapState, stateFactory, characters);
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
    when(characters.fluent()).thenReturn(FluentIterable.<Character>of(enemy, enemy2));
    when(enemy.retaliate()).thenReturn(retaliation);
    when(enemy2.retaliate()).thenReturn(retaliation2);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    retaliating.enter();

    InOrder inOrder = Mockito.inOrder(enemy, enemy2, mapState, eventBus);
    inOrder.verify(eventBus, times(2)).post(argumentCaptor.capture());
    inOrder.verify(enemy).retaliate();
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    inOrder.verify(enemy2).retaliate();
    inOrder.verify(mapState).branchTo(waiting);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(retaliating);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, enemy, ActivatedEnemy.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, enemy2, ActivatedEnemy.class);
  }

  @Test
  public void exit() {
    retaliating.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, retaliating, ExitState.class);
  }
}