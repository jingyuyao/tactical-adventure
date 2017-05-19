package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableListMultimap;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
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
  private LevelComplete levelComplete;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private Script script;
  @Mock
  private Dialogue dialogue;
  @Mock
  private Turn turn;
  @Mock
  private Retaliating retaliating;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private EndTurn endTurn;

  @Before
  public void setUp() {
    endTurn = new EndTurn(modelBus, worldState, levelComplete, stateFactory, world);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);

    endTurn.enter();
  }

  @Test
  public void enter_no_dialogue() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(worldState.getScript()).thenReturn(script);
    when(script.getTurnDialogues()).thenReturn(ImmutableListMultimap.<Turn, Dialogue>of());
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    InOrder inOrder = Mockito.inOrder(modelBus, world, levelComplete, turn, worldState);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(endTurn);
    inOrder.verify(levelComplete).check(runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(world).makeAllPlayerShipsControllable();
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(retaliating);
  }

  @Test
  public void enter_has_dialogue() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(worldState.getScript()).thenReturn(script);
    when(script.getTurnDialogues()).thenReturn(ImmutableListMultimap.of(turn, dialogue));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    InOrder inOrder = Mockito.inOrder(modelBus, world, turn, worldState);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(endTurn);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue);
    showDialogues.complete();
    verify(levelComplete).check(runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(world).makeAllPlayerShipsControllable();
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(retaliating);
  }
}