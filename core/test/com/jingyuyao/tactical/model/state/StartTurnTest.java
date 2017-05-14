package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.TurnScript;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
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
public class StartTurnTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Mock
  private TurnScript turnScript;
  @Mock
  private Dialogue dialogue;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private StartTurn startTurn;

  @Before
  public void setUp() {
    startTurn = new StartTurn(modelBus, worldState, stateFactory);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.END);

    startTurn.enter();
  }

  @Test
  public void enter_no_turn_script() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);
    when(worldState.getScript()).thenReturn(script);
    when(script.turnScript(turn)).thenReturn(Optional.<TurnScript>absent());
    when(stateFactory.createWaiting()).thenReturn(waiting);

    startTurn.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(startTurn);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(Save.class);
    verify(turn).advance();
    verify(worldState).branchTo(waiting);
  }

  @Test
  public void enter_no_dialogue() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);
    when(worldState.getScript()).thenReturn(script);
    when(script.turnScript(turn)).thenReturn(Optional.of(turnScript));
    when(turnScript.getDialogues()).thenReturn(ImmutableList.<Dialogue>of());
    when(stateFactory.createWaiting()).thenReturn(waiting);

    startTurn.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(startTurn);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(Save.class);
    verify(turn).advance();
    verify(worldState).branchTo(waiting);
  }

  @Test
  public void enter_has_dialogue() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);
    when(worldState.getScript()).thenReturn(script);
    when(script.turnScript(turn)).thenReturn(Optional.of(turnScript));
    when(turnScript.getDialogues()).thenReturn(ImmutableList.of(dialogue));
    when(stateFactory.createWaiting()).thenReturn(waiting);

    startTurn.enter();

    InOrder inOrder = Mockito.inOrder(modelBus, turn, worldState);
    inOrder.verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(startTurn);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getAllValues().get(1);
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue);
    showDialogues.complete();
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(waiting);
  }
}