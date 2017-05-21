package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.script.DeathEvent;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptEvent;
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
public class BattleSequenceTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private ScriptRunner scriptRunner;
  @Mock
  private Battle battle;
  @Mock
  private Person dead1;
  @Mock
  private Person dead2;
  @Mock
  private Script script;
  @Mock
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<ScriptEvent> scriptEventCaptor;

  private BattleSequence battleSequence;

  @Before
  public void setUp() {
    battleSequence = new BattleSequence(modelBus, world, worldState, scriptRunner);
  }

  @Test
  public void start() {
    when(worldState.getScript()).thenReturn(script);
    when(battle.getDeaths()).thenReturn(ImmutableList.of(dead1, dead2));
    when(scriptRunner.triggerScripts(any(ScriptEvent.class), eq(script)))
        .thenReturn(Promise.immediate());

    battleSequence.start(battle, runnable);

    InOrder inOrder = Mockito.inOrder(modelBus, battle, scriptRunner, runnable);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(StartBattle.class);
    StartBattle startBattle = (StartBattle) argumentCaptor.getValue();
    startBattle.start();
    inOrder.verify(battle).execute();
    inOrder.verify(scriptRunner, times(2)).triggerScripts(scriptEventCaptor.capture(), eq(script));
    DeathEvent event1 = TestHelpers.assertClass(scriptEventCaptor, 0, DeathEvent.class);
    assertThat(event1.getDeath()).isSameAs(dead1);
    assertThat(event1.getWorld()).isSameAs(world);
    DeathEvent event2 = TestHelpers.assertClass(scriptEventCaptor, 1, DeathEvent.class);
    assertThat(event2.getDeath()).isSameAs(dead2);
    assertThat(event2.getWorld()).isSameAs(world);
    inOrder.verify(runnable).run();
  }
}