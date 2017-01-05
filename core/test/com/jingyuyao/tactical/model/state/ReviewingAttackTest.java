package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;
import com.jingyuyao.tactical.model.state.event.HideAttackPlan;
import com.jingyuyao.tactical.model.state.event.ShowAttackPlan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReviewingAttackTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Markings markings;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private AttackPlan attackPlan;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Waiting waiting;

  private ReviewingAttack reviewingAttack;

  @Before
  public void setUp() {
    reviewingAttack =
        new ReviewingAttack(eventBus, mapState, markings, stateFactory, player, attackPlan);
  }

  @Test
  public void enter() {
    reviewingAttack.enter();

    verify(markings).showImmediateTargets(player);
    verify(eventBus).post(argumentCaptor.capture());
    ShowAttackPlan showAttackPlan =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowAttackPlan.class);
    assertThat(showAttackPlan.getObject()).isSameAs(attackPlan);
  }

  @Test
  public void exit() {
    reviewingAttack.exit();

    verify(markings).clearPlayerMarking();
    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(HideAttackPlan.class);
  }

  @Test
  public void select_player() {
    reviewingAttack.select(player);

    verify(mapState).pop();
  }

  @Test
  public void select_enemy() {
    reviewingAttack.select(enemy);

    verify(mapState).pop();
  }

  @Test
  public void select_terrain() {
    reviewingAttack.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void actions_attack() {
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = actionsSetUp();

    Action attack = actions.get(0);
    attack.run();

    verify(attackPlan).execute();
    verify(player).setActionable(false);
    verify(mapState).newStack(waiting);
    verifyNoMoreInteractions(mapState);
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
}