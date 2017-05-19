package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
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
public class BattleSequenceTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private ScriptRunner scriptRunner;
  @Mock
  private Script script;
  @Mock
  private Battle battle;
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
  private Runnable done;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private BattleSequence battleSequence;

  @Before
  public void setUp() {
    battleSequence = new BattleSequence(modelBus, worldState, scriptRunner);
  }

  @Test
  public void start() {
    when(worldState.getScript()).thenReturn(script);
    when(battle.getDeaths()).thenReturn(ImmutableList.of(dead1, dead2, dead3));
    when(dead1.getName()).thenReturn(name1);
    when(dead2.getName()).thenReturn(name2);
    when(dead3.getName()).thenReturn(name3);
    when(script.getDeathDialogues())
        .thenReturn(ImmutableListMultimap.of(name1, dialogue1, name3, dialogue3));

    battleSequence.start(battle, done);

    InOrder inOrder = Mockito.inOrder(modelBus, battle, scriptRunner);
    verifyZeroInteractions(scriptRunner);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(StartBattle.class);
    StartBattle startBattle = (StartBattle) argumentCaptor.getValue();
    startBattle.start();
    inOrder.verify(battle).execute();
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
    inOrder.verify(scriptRunner).check(done);
  }
}