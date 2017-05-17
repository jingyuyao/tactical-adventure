package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.world.Cell;
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

  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private ModelBus modelBus;
  @Mock
  private BattleSequence battleSequence;
  @Mock
  private Cell playerCell;
  @Mock
  private Player attackingPlayer;
  @Mock
  private Battle battle;
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
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private Battling battling;

  @Before
  public void setUp() {
    when(playerCell.player()).thenReturn(Optional.of(attackingPlayer));
    battling = new Battling(modelBus, worldState, stateFactory, battleSequence, playerCell, battle);
  }

  @Test
  public void enter() {
    battling.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(battling);
  }

  @Test
  public void exit() {
    battling.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, battling, ExitState.class);
  }

  @Test
  public void select_cannot_attack() {
    when(battle.getTarget()).thenReturn(target);
    when(target.canTarget(cell)).thenReturn(false);

    battling.select(cell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void select_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(battle.getTarget()).thenReturn(target);
    when(target.canTarget(cell)).thenReturn(true);

    battling.select(cell);

    verify_attacked();
  }

  @Test
  public void attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);

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
    InOrder inOrder = Mockito.inOrder(modelBus, worldState, attackingPlayer, battleSequence);
    inOrder.verify(worldState).goTo(transition);
    inOrder.verify(battleSequence).start(Mockito.eq(battle), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(attackingPlayer).setControllable(false);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(waiting);
    verifyNoMoreInteractions(worldState);
  }
}