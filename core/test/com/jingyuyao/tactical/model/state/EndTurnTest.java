package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptEvent;
import com.jingyuyao.tactical.model.script.TurnEvent;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
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
public class EndTurnTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private World world;
  @Mock
  private ScriptRunner scriptRunner;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Turn turn;
  @Mock
  private Script script;
  @Mock
  private Retaliating retaliating;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<ScriptEvent> scriptEventCaptor;

  private EndTurn endTurn;

  @Before
  public void setUp() {
    endTurn = new EndTurn(modelBus, worldState, world, scriptRunner, stateFactory);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);

    endTurn.enter();
  }

  @Test
  public void enter() {
    when(worldState.getTurn()).thenReturn(turn);
    when(worldState.getScript()).thenReturn(script);
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(scriptRunner.triggerScripts(any(ScriptEvent.class), eq(script)))
        .thenReturn(Promise.immediate());
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    InOrder inOrder = Mockito.inOrder(modelBus, world, scriptRunner, turn, worldState);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(endTurn);
    inOrder.verify(scriptRunner).triggerScripts(scriptEventCaptor.capture(), eq(script));
    TurnEvent turnEvent = TestHelpers.assertClass(scriptEventCaptor.getValue(), TurnEvent.class);
    assertThat(turnEvent.getTurn()).isSameAs(turn);
    assertThat(turnEvent.getWorld()).isSameAs(world);
    inOrder.verify(world).makeAllPlayerShipsControllable();
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(retaliating);
  }
}