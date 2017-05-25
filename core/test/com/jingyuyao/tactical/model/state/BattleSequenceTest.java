package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptEvent;
import com.jingyuyao.tactical.model.script.ShipDestroyed;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Arrays;
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
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship;
  @Mock
  private Ship ship2;
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
    when(battle.getDeadCells()).thenReturn(Arrays.asList(cell, cell2));
    when(world.removeShip(cell)).thenReturn(ship);
    when(world.removeShip(cell2)).thenReturn(ship2);
    when(scriptRunner.triggerScripts(any(ScriptEvent.class), eq(script)))
        .thenReturn(Promise.immediate());

    battleSequence.start(battle, runnable);

    InOrder inOrder = Mockito.inOrder(modelBus, battle, scriptRunner, runnable, world);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(StartBattle.class);
    StartBattle startBattle = (StartBattle) argumentCaptor.getValue();
    startBattle.start();
    inOrder.verify(battle).execute();
    inOrder.verify(world).removeShip(cell);
    inOrder.verify(scriptRunner).triggerScripts(scriptEventCaptor.capture(), eq(script));
    ShipDestroyed event1 =
        TestHelpers.assertClass(scriptEventCaptor.getValue(), ShipDestroyed.class);
    assertThat(event1.getDestroyed()).isSameAs(ship);
    assertThat(event1.getWorld()).isSameAs(world);
    inOrder.verify(world).removeShip(cell2);
    inOrder.verify(scriptRunner).triggerScripts(scriptEventCaptor.capture(), eq(script));
    ShipDestroyed event2 =
        TestHelpers.assertClass(scriptEventCaptor.getValue(), ShipDestroyed.class);
    assertThat(event2.getDestroyed()).isSameAs(ship2);
    assertThat(event2.getWorld()).isSameAs(world);
    inOrder.verify(runnable).run();
  }
}