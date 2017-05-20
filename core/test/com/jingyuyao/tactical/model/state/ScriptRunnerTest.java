package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
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
public class ScriptRunnerTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private Script script;
  @Mock
  private Turn turn;
  @Mock
  private Condition winCondition;
  @Mock
  private Condition loseCondition;
  @Mock
  private Person dead1;
  @Mock
  private Person dead2;
  @Mock
  private Person dead3;
  @Mock
  private ResourceKey name1;
  @Mock
  private ResourceKey name2;
  @Mock
  private ResourceKey name3;
  @Mock
  private Dialogue dialogue1;
  @Mock
  private Dialogue dialogue3;
  @Mock
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ScriptRunner scriptRunner;

  @Before
  public void setUp() {
    scriptRunner = new ScriptRunner(modelBus, world, worldState);
  }

  @Test
  public void turn_has_dialogue_lost() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getTurnDialogues()).thenReturn(ImmutableListMultimap.of(turn, dialogue1));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.triggerTurn(runnable);

    InOrder inOrder = Mockito.inOrder(modelBus);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    ShowDialogues showDialogues =
        TestHelpers.assertClass(argumentCaptor.getValue(), ShowDialogues.class);
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue1);
    showDialogues.complete();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelLost.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void turn_no_dialogue_won() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getTurnDialogues()).thenReturn(ImmutableListMultimap.<Turn, Dialogue>of());
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.triggerTurn(runnable);

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void turn_keep_going() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getTurnDialogues()).thenReturn(ImmutableListMultimap.<Turn, Dialogue>of());
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(false);

    scriptRunner.triggerTurn(runnable);

    verifyZeroInteractions(modelBus);
    verify(runnable).run();
  }

  @Test
  public void deaths_won() {
    when(worldState.getScript()).thenReturn(script);
    when(dead1.getName()).thenReturn(name1);
    when(dead2.getName()).thenReturn(name2);
    when(dead3.getName()).thenReturn(name3);
    when(script.getDeathDialogues())
        .thenReturn(ImmutableListMultimap.of(name1, dialogue1, name3, dialogue3));
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.triggerDeaths(ImmutableList.of(dead1, dead2, dead3), runnable);

    InOrder inOrder = Mockito.inOrder(modelBus);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue1);
    showDialogues.complete();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues2 = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues2.getDialogues()).containsExactly(dialogue3);
    showDialogues2.complete();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void deaths_keep_going() {
    when(worldState.getScript()).thenReturn(script);
    when(dead1.getName()).thenReturn(name1);
    when(dead2.getName()).thenReturn(name2);
    when(dead3.getName()).thenReturn(name3);
    when(script.getDeathDialogues())
        .thenReturn(ImmutableListMultimap.of(name1, dialogue1, name3, dialogue3));
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(false);

    scriptRunner.triggerDeaths(ImmutableList.of(dead1, dead2, dead3), runnable);

    InOrder inOrder = Mockito.inOrder(modelBus, runnable);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue1);
    showDialogues.complete();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues2 = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues2.getDialogues()).containsExactly(dialogue3);
    showDialogues2.complete();
    inOrder.verify(runnable).run();
  }
}