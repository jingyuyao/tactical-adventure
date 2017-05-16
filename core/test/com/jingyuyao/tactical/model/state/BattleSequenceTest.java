package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
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
  private Script script;
  @Mock
  private Battle battle;
  @Mock
  private Person person1;
  @Mock
  private Person person2;
  @Mock
  private Person person3;
  @Mock
  private Message name1;
  @Mock
  private Message name2;
  @Mock
  private Message name3;
  @Mock
  private ScriptActions actions1;
  @Mock
  private ScriptActions actions3;
  @Mock
  private Runnable done;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private BattleSequence battleSequence;

  @Before
  public void setUp() {
    battleSequence = new BattleSequence(modelBus, worldState);
  }

  @Test
  public void start() {
    when(worldState.getScript()).thenReturn(script);
    when(battle.getDeath()).thenReturn(ImmutableList.of(person1, person2, person3));
    when(person1.getName()).thenReturn(name1);
    when(person2.getName()).thenReturn(name2);
    when(person3.getName()).thenReturn(name3);
    when(script.deathScript(name1)).thenReturn(Optional.of(actions1));
    when(script.deathScript(name2)).thenReturn(Optional.<ScriptActions>absent());
    when(script.deathScript(name3)).thenReturn(Optional.of(actions3));

    battleSequence.start(battle, done);

    InOrder inOrder = Mockito.inOrder(modelBus, actions1, actions3, battle, done);
    verifyZeroInteractions(done);
    verifyZeroInteractions(actions1);
    verifyZeroInteractions(actions3);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(StartBattle.class);
    StartBattle startBattle = (StartBattle) argumentCaptor.getValue();
    startBattle.start();
    inOrder.verify(battle).execute();
    inOrder.verify(actions1).execute(Mockito.eq(modelBus), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(actions3).execute(Mockito.eq(modelBus), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(done).run();
  }
}