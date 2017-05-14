package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.script.TurnScript;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
  private TurnScript turnScript;
  @Mock
  private ScriptActions scriptActions;
  @Mock
  private Dialogue dialogue;
  @Mock
  private Retaliating retaliating;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private EndTurn endTurn;

  @Before
  public void setUp() {
    endTurn = new EndTurn(modelBus, worldState, stateFactory, world);
  }

  @Test
  public void enter_no_turn_script() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(3);
    when(script.turnScript(3)).thenReturn(Optional.<TurnScript>absent());
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(1);
    verify(player).setActionable(true);
    verify(worldState).incrementTurn();
    verify(worldState).branchTo(retaliating);
  }

  @Test
  public void enter_no_dialogue() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(3);
    when(script.turnScript(3)).thenReturn(Optional.of(turnScript));
    when(turnScript.getEnd()).thenReturn(scriptActions);
    when(scriptActions.getDialogues()).thenReturn(ImmutableList.<Dialogue>of());
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(1);
    verify(player).setActionable(true);
    verify(worldState).incrementTurn();
    verify(worldState).branchTo(retaliating);
  }

  @Test
  public void enter_has_dialogue() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(3);
    when(script.turnScript(3)).thenReturn(Optional.of(turnScript));
    when(turnScript.getEnd()).thenReturn(scriptActions);
    when(scriptActions.getDialogues()).thenReturn(ImmutableList.of(dialogue));
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getAllValues().get(1);
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue);
    showDialogues.complete();
    verify(player).setActionable(true);
    verify(worldState).incrementTurn();
    verify(worldState).branchTo(retaliating);
  }
}