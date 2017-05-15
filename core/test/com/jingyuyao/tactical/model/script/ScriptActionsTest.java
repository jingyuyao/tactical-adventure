package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
    actions = new ScriptActions(ImmutableList.of(dialogue));

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
    actions = new ScriptActions(ImmutableList.<Dialogue>of());

    actions.execute(modelBus, runnable);

    verify(runnable).run();
    verifyZeroInteractions(modelBus);
  }
}