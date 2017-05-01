package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaitingTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private Movements movements;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Player player;
  @Mock
  private Moving moving;
  @Mock
  private Retaliating retaliating;
  @Mock
  private Movement movement;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(modelBus, worldState, stateFactory, world, movements);
  }

  @Test
  public void enter_not_complete() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.player()).thenReturn(Optional.of(player));
    when(cell2.player()).thenReturn(Optional.<Player>absent());
    when(cell2.hasEnemy()).thenReturn(true);

    waiting.enter();

    verify(modelBus).post(waiting);
    verifyNoMoreInteractions(modelBus);
  }

  @Test
  public void enter_level_complete() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));

    waiting.enter();

    verify(player).setActionable(true);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(waiting);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(LevelComplete.class);
  }

  @Test
  public void enter_level_failed() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.<Player>absent());
    when(cell.hasEnemy()).thenReturn(true);

    waiting.enter();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(waiting);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(LevelFailed.class);
  }

  @Test
  public void exit() {
    waiting.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, waiting, ExitState.class);
  }

  @Test
  public void select_player_actionable() {
    when(player.isActionable()).thenReturn(true);
    when(movements.distanceFrom(cell)).thenReturn(movement);
    when(stateFactory.createMoving(cell, movement)).thenReturn(moving);
    when(cell.player()).thenReturn(Optional.of(player));

    waiting.select(cell);

    verify(stateFactory).createMoving(cell, movement);
    verify(worldState).goTo(moving);
  }

  @Test
  public void select_player_not_actionable() {
    when(player.isActionable()).thenReturn(false);
    when(cell.player()).thenReturn(Optional.of(player));

    waiting.select(cell);

    verifyZeroInteractions(stateFactory);
    verifyZeroInteractions(worldState);
  }

  @Test
  public void end_turn() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    waiting.endTurn();

    verify(player).setActionable(true);
    verify(worldState).goTo(retaliating);
  }

  @Test
  public void actions() {
    List<Action> actions = waiting.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(EndTurnAction.class);
  }
}
