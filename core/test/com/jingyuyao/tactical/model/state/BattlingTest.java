package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedPlayer;
import com.jingyuyao.tactical.model.event.DeactivatedPlayer;
import com.jingyuyao.tactical.model.event.HideTarget;
import com.jingyuyao.tactical.model.event.ShowTarget;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
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
  private Enemy enemy;
  @Mock
  private Terrain terrain;
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

    verify(eventBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, attackingPlayer, ActivatedPlayer.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, target, ShowTarget.class);
  }

  @Test
  public void exit() {
    battling.exit();

    verify(eventBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyModelEvent(argumentCaptor, 0, DeactivatedPlayer.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, target, HideTarget.class);
  }

  @Test
  public void select_player_cannot_attack() {
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    battling.select(attackingPlayer);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_player_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(battle.begin(attackingPlayer, weapon, target))
        .thenReturn(Futures.<Void>immediateFuture(null));

    battling.select(attackingPlayer);

    verify_attacked();
  }

  @Test
  public void select_enemy_cannot_attack() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    battling.select(enemy);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_enemy_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(battle.begin(attackingPlayer, weapon, target))
        .thenReturn(Futures.<Void>immediateFuture(null));

    battling.select(enemy);

    verify_attacked();
  }

  @Test
  public void select_terrain_cannot_attack() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    battling.select(terrain);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_terrain_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(battle.begin(attackingPlayer, weapon, target))
        .thenReturn(Futures.<Void>immediateFuture(null));

    battling.select(terrain);

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