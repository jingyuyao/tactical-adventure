package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptActionsTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private Dialogue dialogue;
  @Mock
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ScriptActions actions;

  @Test
  public void execute_has_dialogue() {
    actions = new ScriptActions(ImmutableList.of(dialogue), LevelTrigger.NONE);

    actions.execute(modelBus, runnable);

    verifyZeroInteractions(runnable);
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue);
    showDialogues.complete();
    verify(runnable).run();
  }

  @Test
  public void execute_no_dialogue() {
    actions = new ScriptActions(ImmutableList.<Dialogue>of(), LevelTrigger.NONE);

    actions.execute(modelBus, runnable);

    verify(runnable).run();
    verifyZeroInteractions(modelBus);
  }

  @Test
  public void execute_dialogue_level_won() {
    actions = new ScriptActions(ImmutableList.of(dialogue), LevelTrigger.WON);

    actions.execute(modelBus, runnable);

    InOrder inOrder = Mockito.inOrder(modelBus, runnable);
    verifyZeroInteractions(runnable);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue);
    showDialogues.complete();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    inOrder.verify(runnable).run();
  }

  @Test
  public void execute_no_dialogue_level_lost() {
    actions = new ScriptActions(ImmutableList.<Dialogue>of(), LevelTrigger.LOST);

    actions.execute(modelBus, runnable);

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelLost.class);
    verify(runnable).run();
  }
}