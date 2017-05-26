package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.List;
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
  private Cell cell;
  @Mock
  private Ship attackingShip;
  @Mock
  private Battle battle;
  @Mock
  private Target target;
  @Mock
  private Cell selectCell;
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
    when(cell.ship()).thenReturn(Optional.of(attackingShip));
    when(attackingShip.isControllable()).thenReturn(true);
    battling = new Battling(modelBus, worldState, stateFactory, battleSequence, cell, battle);
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
    when(target.canTarget(selectCell)).thenReturn(false);

    battling.select(selectCell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void select_can_attack() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(battle.getTarget()).thenReturn(target);
    when(target.canTarget(selectCell)).thenReturn(true);

    battling.select(selectCell);

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
    List<Action> actions = battling.getActions();
    assertThat(actions).hasSize(2);
    assertThat(actions.get(0)).isInstanceOf(AttackAction.class);
    assertThat(actions.get(1)).isInstanceOf(BackAction.class);
  }

  private void verify_attacked() {
    InOrder inOrder = Mockito.inOrder(modelBus, worldState, attackingShip, battleSequence);
    inOrder.verify(worldState).branchTo(transition);
    inOrder.verify(battleSequence).start(Mockito.eq(battle), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(attackingShip).setControllable(false);
    inOrder.verify(worldState).branchTo(transition);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    Save save = TestHelpers.assertClass(argumentCaptor.getValue(), Save.class);
    save.complete();
    inOrder.verify(worldState).branchTo(waiting);
    verifyNoMoreInteractions(worldState);
  }
}