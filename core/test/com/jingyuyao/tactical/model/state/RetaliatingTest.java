package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.AutoPilot;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
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
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private World world;
  @Mock
  private BattleSequence battleSequence;
  @Mock
  private Turn turn;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Ship enemy;
  @Mock
  private Ship enemy2;
  @Mock
  private AutoPilot autoPilot;
  @Mock
  private AutoPilot autoPilot2;
  @Mock
  private Path path;
  @Mock
  private Battle battle;
  @Mock
  private Cell origin;
  @Mock
  private StartTurn startTurn;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private Retaliating retaliating;

  @Before
  public void setUp() {
    retaliating =
        new Retaliating(modelBus, worldState, stateFactory, movements, world, battleSequence);
  }

  @Test
  public void select() {
    retaliating.select(cell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void actions() {
    assertThat(retaliating.getActions()).isEmpty();
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);

    retaliating.enter();
  }

  @Test
  public void enter() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.ENEMY);
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.ship()).thenReturn(Optional.of(enemy));
    when(cell2.ship()).thenReturn(Optional.of(enemy2));
    when(enemy.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(enemy2.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(enemy.getAutoPilot(movements, cell)).thenReturn(autoPilot);
    when(enemy2.getAutoPilot(movements, cell2)).thenReturn(autoPilot2);
    when(autoPilot.path()).thenReturn(Optional.of(path));
    when(autoPilot.battle()).thenReturn(Optional.of(battle));
    when(autoPilot2.path()).thenReturn(Optional.<Path>absent());
    when(autoPilot2.battle()).thenReturn(Optional.<Battle>absent());
    when(path.getOrigin()).thenReturn(origin);
    when(origin.moveShip(path)).thenReturn(Promise.immediate());
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    retaliating.enter();

    InOrder inOrder =
        Mockito.inOrder(enemy, enemy2, worldState, modelBus, origin, turn, battleSequence);
    inOrder.verify(modelBus, times(2)).post(argumentCaptor.capture());
    inOrder.verify(enemy).getAutoPilot(movements, cell);
    inOrder.verify(origin).moveShip(path);
    inOrder.verify(battleSequence).start(Mockito.eq(battle), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    inOrder.verify(enemy2).getAutoPilot(movements, cell2);
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    inOrder.verify(worldState).branchTo(startTurn);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(retaliating);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, enemy, ActivatedEnemy.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, enemy2, ActivatedEnemy.class);
    assertThat(argumentCaptor.getAllValues().get(3)).isInstanceOf(Save.class);
  }

  @Test
  public void exit() {
    retaliating.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, retaliating, ExitState.class);
  }
}