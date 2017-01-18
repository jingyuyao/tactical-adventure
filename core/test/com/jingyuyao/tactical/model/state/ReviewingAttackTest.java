package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.target.Target;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReviewingAttackTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player attackingPlayer;
  @Mock
  private Target target;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Waiting waiting;
  @Mock
  private ImmutableSet<Coordinate> selectCoordinates;

  private ReviewingAttack reviewingAttack;

  @Before
  public void setUp() {
    reviewingAttack =
        new ReviewingAttack(eventBus, mapState, stateFactory, attackingPlayer, target);
  }

  @Test
  public void enter() {
    reviewingAttack.enter();

    verify(target).showMarking();
  }

  @Test
  public void exit() {
    reviewingAttack.exit();

    verify(target).hideMarking();
  }

  @Test
  public void select_player_cannot_attack() {
    set_up_target(false);
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);

    reviewingAttack.select(attackingPlayer);

    verify(mapState).pop();
  }

  @Test
  public void select_player_can_attack() {
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
    set_up_target(true);

    reviewingAttack.select(attackingPlayer);

    verify_attacked();
  }

  @Test
  public void select_enemy_cannot_attack() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    set_up_target(false);

    reviewingAttack.select(enemy);

    verify(mapState).pop();
  }

  @Test
  public void select_enemy_can_attack() {
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    set_up_target(true);

    reviewingAttack.select(enemy);

    verify_attacked();
  }

  @Test
  public void select_terrain_cannot_attack() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    set_up_target(false);

    reviewingAttack.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void select_terrain_can_attack() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    set_up_target(true);

    reviewingAttack.select(terrain);

    verify_attacked();
  }

  @Test
  public void actions_attack() {
    when(stateFactory.createWaiting()).thenReturn(waiting);
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

  private void set_up_target(boolean canAttack) {
    when(target.getSelectCoordinates()).thenReturn(selectCoordinates);
    when(selectCoordinates.contains(COORDINATE)).thenReturn(canAttack);
    when(attackingPlayer.getCoordinate()).thenReturn(COORDINATE);
  }

  private void verify_attacked() {
    verify(target).execute();
    verify(attackingPlayer).setActionable(false);
    verify(mapState).newStack(waiting);
    verifyNoMoreInteractions(mapState);
  }
}