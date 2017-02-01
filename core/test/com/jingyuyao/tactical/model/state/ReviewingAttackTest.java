package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
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
public class ReviewingAttackTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
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
  private IgnoreInput ignoreInput;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ReviewingAttack reviewingAttack;

  @Before
  public void setUp() {
    reviewingAttack = new ReviewingAttack(mapState, stateFactory, eventBus, attackingPlayer, weapon,
        target);
  }

  @Test
  public void enter() {
    reviewingAttack.enter();

    verify(target).showMarking();
    verify(eventBus).post(argumentCaptor.capture());
    ShowTarget showTarget = TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowTarget.class);
    assertThat(showTarget.getObject()).isSameAs(target);
  }

  @Test
  public void exit() {
    reviewingAttack.exit();

    verify(target).hideMarking();
    verify(eventBus).post(argumentCaptor.capture());
    HideTarget hideTarget = TestHelpers.isInstanceOf(argumentCaptor.getValue(), HideTarget.class);
    assertThat(hideTarget.getObject()).isSameAs(target);
  }

  @Test
  public void select_player_cannot_attack() {
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    reviewingAttack.select(attackingPlayer);

    verify(mapState).back();
  }

  @Test
  public void select_player_can_attack() {
    when(stateFactory.createIgnoreInput()).thenReturn(ignoreInput);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(attackingPlayer.attacks(weapon, target)).thenReturn(Futures.<Void>immediateFuture(null));

    reviewingAttack.select(attackingPlayer);

    verify_attacked();
  }

  @Test
  public void select_enemy_cannot_attack() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    reviewingAttack.select(enemy);

    verify(mapState).back();
  }

  @Test
  public void select_enemy_can_attack() {
    when(stateFactory.createIgnoreInput()).thenReturn(ignoreInput);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(attackingPlayer.attacks(weapon, target)).thenReturn(Futures.<Void>immediateFuture(null));

    reviewingAttack.select(enemy);

    verify_attacked();
  }

  @Test
  public void select_terrain_cannot_attack() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(false);

    reviewingAttack.select(terrain);

    verify(mapState).back();
  }

  @Test
  public void select_terrain_can_attack() {
    when(stateFactory.createIgnoreInput()).thenReturn(ignoreInput);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target.canTarget(COORDINATE)).thenReturn(true);
    when(attackingPlayer.attacks(weapon, target)).thenReturn(Futures.<Void>immediateFuture(null));

    reviewingAttack.select(terrain);

    verify_attacked();
  }

  @Test
  public void actions_attack() {
    when(stateFactory.createIgnoreInput()).thenReturn(ignoreInput);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(attackingPlayer.attacks(weapon, target)).thenReturn(Futures.<Void>immediateFuture(null));
    ImmutableList<Action> actions = actionsSetUp();

    Action attack = actions.get(0);
    attack.run();

    verify_attacked();
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = actionsSetUp();

    StateHelpers.verifyBack(actions.get(1), mapState);
  }

  private ImmutableList<Action> actionsSetUp() {
    ImmutableList<Action> actions = reviewingAttack.getActions();
    assertThat(actions).hasSize(2);
    return actions;
  }

  private void verify_attacked() {
    InOrder inOrder = Mockito.inOrder(mapState, weapon, attackingPlayer);
    inOrder.verify(mapState).goTo(ignoreInput);
    inOrder.verify(attackingPlayer).attacks(weapon, target);
    inOrder.verify(attackingPlayer).setActionable(false);
    inOrder.verify(mapState).branchTo(waiting);
    verifyNoMoreInteractions(mapState);
  }
}