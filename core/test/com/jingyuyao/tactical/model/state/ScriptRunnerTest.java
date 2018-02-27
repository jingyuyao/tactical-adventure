package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_2;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.DeactivateGroup;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptEvent;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
  private Script script;
  @Mock
  private ScriptEvent scriptEvent;
  @Mock
  private Condition dialogueCondition1;
  @Mock
  private Condition dialogueCondition2;
  @Mock
  private Condition dialogueCondition3;
  @Mock
  private Condition dialogueCondition4;
  @Mock
  private Condition activationCondition;
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
  private ActivateGroup activateGroup;
  @Mock
  private DeactivateGroup deactivateGroup;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<List<Cell>> cellsCaptor;

  private ScriptRunner scriptRunner;

  @Before
  public void setUp() {
    scriptRunner = new ScriptRunner(modelBus, world);
  }

  @Test
  public void has_dialogue_lost() {
    when(script.getDialogues())
        .thenReturn(ImmutableMap.of(
            dialogueCondition1, Collections.singletonList(dialogue1),
            dialogueCondition2, Collections.singletonList(dialogue2),
            dialogueCondition3, Collections.singletonList(dialogue3),
            dialogueCondition4, Collections.singletonList(dialogue4)));
    when(script.getGroupActivations()).thenReturn(Collections.<Condition, ActivateGroup>emptyMap());
    when(script.getGroupDeactivations())
        .thenReturn(Collections.<Condition, DeactivateGroup>emptyMap());
    when(script.getLoseConditions()).thenReturn(Collections.singletonList(loseCondition));
    when(scriptEvent.satisfiedBy(dialogueCondition1)).thenReturn(true); // show
    when(scriptEvent.satisfiedBy(dialogueCondition2)).thenReturn(false);
    when(scriptEvent.satisfiedBy(dialogueCondition3)).thenReturn(false);
    when(scriptEvent.satisfiedBy(dialogueCondition4)).thenReturn(true); // show
    when(scriptEvent.satisfiedBy(loseCondition)).thenReturn(true);

    Promise promise = scriptRunner.triggerScripts(scriptEvent, script);

    InOrder inOrder =
        Mockito.inOrder(modelBus, loseCondition, dialogueCondition1, dialogueCondition4);
    assertThat(promise.isDone()).isFalse();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues.getDialogues()).containsExactly(dialogue1);
    showDialogues.complete();
    assertThat(promise.isDone()).isFalse();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(ShowDialogues.class);
    ShowDialogues showDialogues2 = (ShowDialogues) argumentCaptor.getValue();
    assertThat(showDialogues2.getDialogues()).containsExactly(dialogue4);
    showDialogues2.complete();
    assertThat(promise.isDone()).isFalse();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelLost.class);
    assertThat(promise.isDone()).isFalse();
  }

  @Test
  public void no_dialogue_won() {
    when(script.getDialogues()).thenReturn(Collections.<Condition, List<Dialogue>>emptyMap());
    when(script.getGroupActivations()).thenReturn(Collections.<Condition, ActivateGroup>emptyMap());
    when(script.getGroupDeactivations())
        .thenReturn(Collections.<Condition, DeactivateGroup>emptyMap());
    when(script.getWinConditions()).thenReturn(Collections.singletonList(winCondition));
    when(script.getLoseConditions()).thenReturn(Collections.singletonList(loseCondition));
    when(scriptEvent.satisfiedBy(loseCondition)).thenReturn(false);
    when(scriptEvent.satisfiedBy(winCondition)).thenReturn(true);

    Promise promise = scriptRunner.triggerScripts(scriptEvent, script);

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    assertThat(promise.isDone()).isFalse();
  }

  @Test
  public void has_activations() {
    ShipGroup group = new ShipGroup("hello");
    when(script.getDialogues()).thenReturn(Collections.<Condition, List<Dialogue>>emptyMap());
    when(script.getGroupActivations())
        .thenReturn(ImmutableMap.of(activationCondition, activateGroup));
    when(script.getGroupDeactivations())
        .thenReturn(Collections.<Condition, DeactivateGroup>emptyMap());
    when(script.getWinConditions()).thenReturn(Collections.singletonList(winCondition));
    when(script.getLoseConditions()).thenReturn(Collections.singletonList(loseCondition));
    when(world.cell(C0_0)).thenReturn(Optional.of(cell1));
    when(world.cell(C0_1)).thenReturn(Optional.<Cell>absent());
    when(world.cell(C0_2)).thenReturn(Optional.of(cell2));
    when(activateGroup.getGroup()).thenReturn(group);
    when(activateGroup.getSpawns()).thenReturn(Arrays.asList(C0_0, C0_1, C0_2));
    when(cell1.ship()).thenReturn(Optional.<Ship>absent());
    when(cell2.ship()).thenReturn(Optional.of(ship1));
    when(scriptEvent.satisfiedBy(activationCondition)).thenReturn(true);
    when(scriptEvent.satisfiedBy(loseCondition)).thenReturn(false);
    when(scriptEvent.satisfiedBy(winCondition)).thenReturn(false);

    Promise promise = scriptRunner.triggerScripts(scriptEvent, script);

    verifyZeroInteractions(modelBus);
    verify(world).activateGroup(eq(group), cellsCaptor.capture());
    assertThat(cellsCaptor.getValue()).containsExactly(cell1);
    assertThat(promise.isDone()).isTrue();
  }

  @Test
  public void has_deactivation() {
    ShipGroup group = new ShipGroup("hello");
    when(script.getDialogues()).thenReturn(Collections.<Condition, List<Dialogue>>emptyMap());
    when(script.getGroupActivations()).thenReturn(Collections.<Condition, ActivateGroup>emptyMap());
    when(script.getGroupDeactivations())
        .thenReturn(ImmutableMap.of(activationCondition, deactivateGroup));
    when(script.getWinConditions()).thenReturn(Collections.singletonList(winCondition));
    when(script.getLoseConditions()).thenReturn(Collections.singletonList(loseCondition));
    when(deactivateGroup.getGroup()).thenReturn(group);
    when(scriptEvent.satisfiedBy(activationCondition)).thenReturn(true);
    when(scriptEvent.satisfiedBy(loseCondition)).thenReturn(false);
    when(scriptEvent.satisfiedBy(winCondition)).thenReturn(false);

    Promise promise = scriptRunner.triggerScripts(scriptEvent, script);

    verifyZeroInteractions(modelBus);
    verify(world).deactivateGroup(group);
    assertThat(promise.isDone()).isTrue();
  }

  @Test
  public void no_dialogue_keep_going() {
    when(script.getDialogues()).thenReturn(Collections.<Condition, List<Dialogue>>emptyMap());
    when(script.getGroupActivations()).thenReturn(Collections.<Condition, ActivateGroup>emptyMap());
    when(script.getGroupDeactivations())
        .thenReturn(Collections.<Condition, DeactivateGroup>emptyMap());
    when(script.getWinConditions()).thenReturn(Collections.singletonList(winCondition));
    when(script.getLoseConditions()).thenReturn(Collections.singletonList(loseCondition));
    when(scriptEvent.satisfiedBy(loseCondition)).thenReturn(false);
    when(scriptEvent.satisfiedBy(winCondition)).thenReturn(false);

    Promise promise = scriptRunner.triggerScripts(scriptEvent, script);

    verifyZeroInteractions(modelBus);
    assertThat(promise.isDone()).isTrue();
  }
}