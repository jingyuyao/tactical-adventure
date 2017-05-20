package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.ShowDialogues;
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
  private Condition dialogueCondition1;
  @Mock
  private Condition dialogueCondition2;
  @Mock
  private Condition dialogueCondition3;
  @Mock
  private Condition dialogueCondition4;
  @Mock
  private Condition winCondition;
  @Mock
  private Condition loseCondition;
  @Mock
  private Dialogue dialogue1;
  @Mock
  private Dialogue dialogue2;
  @Mock
  private Dialogue dialogue3;
  @Mock
  private Dialogue dialogue4;
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
  public void has_dialogue_lost() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getDialogues())
        .thenReturn(ImmutableListMultimap.of(
            dialogueCondition1, dialogue1,
            dialogueCondition2, dialogue2,
            dialogueCondition3, dialogue3,
            dialogueCondition4, dialogue4));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(dialogueCondition1.isMet(turn, world)).thenReturn(true); // show
    when(dialogueCondition2.isMet(turn, world)).thenReturn(false);
    when(dialogueCondition3.isTriggered()).thenReturn(true);
    when(dialogueCondition4.isMet(turn, world)).thenReturn(true); // show
    when(loseCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.triggerScripts(runnable);

    InOrder inOrder =
        Mockito.inOrder(modelBus, loseCondition, dialogueCondition1, dialogueCondition4);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue1);
    showDialogues.complete();
    inOrder.verify(dialogueCondition1).triggered();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues2 = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues2.getDialogues()).containsExactly(dialogue4);
    showDialogues2.complete();
    inOrder.verify(dialogueCondition4).triggered();
    inOrder.verify(loseCondition).triggered();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelLost.class);
    verify(dialogueCondition2, never()).triggered();
    verifyZeroInteractions(runnable);
  }

  @Test
  public void no_dialogue_won() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getDialogues()).thenReturn(ImmutableListMultimap.<Condition, Dialogue>of());
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.triggerScripts(runnable);

    verify(loseCondition, never()).triggered();
    verify(winCondition).triggered();
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void no_dialogue_keep_going() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getDialogues()).thenReturn(ImmutableListMultimap.<Condition, Dialogue>of());
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(false);

    scriptRunner.triggerScripts(runnable);

    verify(loseCondition, never()).triggered();
    verify(winCondition, never()).triggered();
    verifyZeroInteractions(modelBus);
    verify(runnable).run();
  }
}