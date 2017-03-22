package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
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
public class BattlingTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
  @Mock
  private Battle battle;
  @Mock
  private Player attackingPlayer;
  @Mock
  private Weapon weapon;
  @Mock
  private Target target;
  @Mock
  private Cell cell;
  @Mock
  private Waiting waiting;
  @Mock
  private Transition transition;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Battling battling;

  @Before
  public void setUp() {
    battling = new Battling(eventBus, mapState, stateFactory, battle, attackingPlayer,
        weapon,
        target);
  }

  @Test
  public void enter() {
    battling.enter();

    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(battling);
  }

  @Test
  public void exit() {
    battling.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, battling, ExitState.class);
  }

  @Test
  public void select_cannot_attack() {
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    battling.select(cell);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(battle.begin(attackingPlayer, weapon, target))
        .thenReturn(Futures.<Void>immediateFuture(null));

    battling.select(cell);

    verify_attacked();
  }

  @Test
  public void attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(battle.begin(attackingPlayer, weapon, target))
        .thenReturn(Futures.<Void>immediateFuture(null));

    battling.attack();

    verify_attacked();
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = battling.getActions();
    assertThat(actions).hasSize(2);
    assertThat(actions.get(0)).isInstanceOf(AttackAction.class);
    assertThat(actions.get(1)).isInstanceOf(BackAction.class);
  }

  private void verify_attacked() {
    InOrder inOrder = Mockito.inOrder(mapState, weapon, attackingPlayer, battle);
    inOrder.verify(mapState).goTo(transition);
    inOrder.verify(battle).begin(attackingPlayer, weapon, target);
    inOrder.verify(attackingPlayer).setActionable(false);
    inOrder.verify(mapState).branchTo(waiting);
    verifyNoMoreInteractions(mapState);
  }
}