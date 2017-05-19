package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
  private Runnable runnable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ScriptRunner scriptRunner;

  @Before
  public void setUp() {
    scriptRunner = new ScriptRunner(modelBus, world, worldState);
  }

  @Test
  public void lost() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.check(runnable);

    verify(script, never()).getWinConditions();
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelLost.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void won() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(true);

    scriptRunner.check(runnable);

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(LevelWon.class);
    verifyZeroInteractions(runnable);
  }

  @Test
  public void not_complete() {
    when(worldState.getScript()).thenReturn(script);
    when(worldState.getTurn()).thenReturn(turn);
    when(script.getWinConditions()).thenReturn(ImmutableList.of(winCondition));
    when(script.getLoseConditions()).thenReturn(ImmutableList.of(loseCondition));
    when(loseCondition.isMet(turn, world)).thenReturn(false);
    when(winCondition.isMet(turn, world)).thenReturn(false);

    scriptRunner.check(runnable);

    verifyZeroInteractions(modelBus);
    verify(runnable).run();
  }
}