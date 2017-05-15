package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
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
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private Cell cell;
  @Mock
  private Player player;
  @Mock
  private Script script;
  @Mock
  private ScriptActions scriptActions;
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
    endTurn = new EndTurn(modelBus, worldState, stateFactory, world);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.START);

    endTurn.enter();
  }

  @Test
  public void enter_no_script() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(worldState.getScript()).thenReturn(script);
    when(script.turnScript(turn)).thenReturn(Optional.<ScriptActions>absent());
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(endTurn);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(Save.class);
    verify(player).setActionable(true);
    verify(turn).advance();
    verify(worldState).branchTo(retaliating);
  }

  @Test
  public void enter_has_script() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(worldState.getScript()).thenReturn(script);
    when(script.turnScript(turn)).thenReturn(Optional.of(scriptActions));
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    InOrder inOrder = Mockito.inOrder(modelBus, player, turn, worldState, scriptActions);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(endTurn);
    inOrder.verify(scriptActions).execute(Mockito.eq(modelBus), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(player).setActionable(true);
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    inOrder.verify(worldState).branchTo(retaliating);
  }
}