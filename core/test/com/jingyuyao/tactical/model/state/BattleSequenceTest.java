package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.StartBattle;
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
  private ScriptRunner scriptRunner;
  @Mock
  private Battle battle;
  @Mock
  private Runnable done;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private BattleSequence battleSequence;

  @Before
  public void setUp() {
    battleSequence = new BattleSequence(modelBus, scriptRunner);
  }

  @Test
  public void start() {
    battleSequence.start(battle, done);

    InOrder inOrder = Mockito.inOrder(modelBus, battle, scriptRunner);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(StartBattle.class);
    StartBattle startBattle = (StartBattle) argumentCaptor.getValue();
    startBattle.start();
    inOrder.verify(battle).execute();
    inOrder.verify(scriptRunner).triggerScripts(done);
  }
}