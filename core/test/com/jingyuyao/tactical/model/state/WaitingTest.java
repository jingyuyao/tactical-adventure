package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Enemy;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaitingTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private Movements movements;
  @Mock
  private Turn turn;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Ship player;
  @Mock
  private Enemy enemy;
  @Mock
  private Moving moving;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Movement movement;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(modelBus, worldState, stateFactory, world, movements);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.ENEMY);

    waiting.enter();
  }

  @Test
  public void enter_not_complete() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.player()).thenReturn(Optional.of(player));
    when(cell.enemy()).thenReturn(Optional.<Enemy>absent());
    when(cell2.player()).thenReturn(Optional.<Ship>absent());
    when(cell2.enemy()).thenReturn(Optional.of(enemy));

    waiting.enter();

    verify(modelBus).post(waiting);
    verifyNoMoreInteractions(modelBus);
  }

  @Test
  public void enter_level_complete() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(cell.enemy()).thenReturn(Optional.<Enemy>absent());

    waiting.enter();

    verify(player).setControllable(true);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(waiting);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(LevelComplete.class);
  }

  @Test
  public void enter_level_failed() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.<Ship>absent());
    when(cell.enemy()).thenReturn(Optional.of(enemy));

    waiting.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(waiting);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(LevelFailed.class);
  }

  @Test
  public void exit() {
    waiting.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, waiting, ExitState.class);
  }

  @Test
  public void select_player_actionable() {
    when(player.isControllable()).thenReturn(true);
    when(movements.distanceFrom(cell)).thenReturn(movement);
    when(stateFactory.createMoving(cell, movement)).thenReturn(moving);
    when(cell.player()).thenReturn(Optional.of(player));

    waiting.select(cell);

    verify(stateFactory).createMoving(cell, movement);
    verify(worldState).goTo(moving);
  }

  @Test
  public void select_player_not_actionable() {
    when(player.isControllable()).thenReturn(false);
    when(cell.player()).thenReturn(Optional.of(player));

    waiting.select(cell);

    verifyZeroInteractions(stateFactory);
    verifyZeroInteractions(worldState);
  }

  @Test
  public void end_turn() {
    when(worldState.getTurn()).thenReturn(turn);
    when(stateFactory.createEndTurn()).thenReturn(endTurn);

    waiting.endTurn();

    verify(turn).advance();
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    verify(worldState).branchTo(endTurn);
  }

  @Test
  public void actions() {
    List<Action> actions = waiting.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(EndTurnAction.class);
  }
}
