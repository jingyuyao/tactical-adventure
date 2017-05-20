package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.PilotResponse;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
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
  private ScriptRunner scriptRunner;
  @Mock
  private StateFactory stateFactory;
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
  private PilotResponse pilotResponse;
  @Mock
  private PilotResponse pilotResponse2;
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
        new Retaliating(modelBus, worldState, scriptRunner, stateFactory, world, battleSequence);
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
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell, enemy, cell2, enemy2));
    when(enemy.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(enemy2.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(enemy.getAutoPilotResponse(world, cell)).thenReturn(pilotResponse);
    when(enemy2.getAutoPilotResponse(world, cell2)).thenReturn(pilotResponse2);
    when(pilotResponse.path()).thenReturn(Optional.of(path));
    when(pilotResponse.battle()).thenReturn(Optional.of(battle));
    when(pilotResponse2.path()).thenReturn(Optional.<Path>absent());
    when(pilotResponse2.battle()).thenReturn(Optional.<Battle>absent());
    when(path.getOrigin()).thenReturn(origin);
    when(origin.moveShip(path)).thenReturn(Promise.immediate());
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    retaliating.enter();

    InOrder inOrder =
        inOrder(enemy, enemy2, worldState, modelBus, origin, turn, battleSequence, scriptRunner);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(retaliating);
    inOrder.verify(scriptRunner).triggerScripts(runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    ActivatedEnemy activatedEnemy1 =
        TestHelpers.assertClass(argumentCaptor.getValue(), ActivatedEnemy.class);
    assertThat(activatedEnemy1.getObject()).isSameAs(enemy);
    inOrder.verify(enemy).getAutoPilotResponse(world, cell);
    inOrder.verify(origin).moveShip(path);
    inOrder.verify(battleSequence).start(Mockito.eq(battle), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    ActivatedEnemy activatedEnemy2 =
        TestHelpers.assertClass(argumentCaptor.getValue(), ActivatedEnemy.class);
    assertThat(activatedEnemy2.getObject()).isSameAs(enemy2);
    inOrder.verify(enemy2).getAutoPilotResponse(world, cell2);
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(startTurn);
  }

  @Test
  public void exit() {
    retaliating.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, retaliating, ExitState.class);
  }
}