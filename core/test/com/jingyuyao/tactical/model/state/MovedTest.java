package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
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
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovedTest {

  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private ModelBus modelBus;
  @Mock
  private Movements movements;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Player player;
  @Mock
  private Player otherPlayer;
  @Mock
  private Movement movement;
  @Mock
  private Moving moving;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Moved moved;

  @Before
  public void setUp() {
    when(cell.player()).thenReturn(Optional.of(player));
    moved = new Moved(modelBus, worldState, stateFactory, movements, cell);
  }

  @Test
  public void enter() {
    moved.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(moved);
  }

  @Test
  public void exit() {
    moved.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, moved, ExitState.class);
  }

  @Test
  public void select_player() {
    when(cell.player()).thenReturn(Optional.of(player));

    moved.select(cell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(cell.player()).thenReturn(Optional.of(otherPlayer));
    when(otherPlayer.canControl()).thenReturn(false);

    moved.select(cell);

    verify(worldState).rollback();
    verifyNoMoreInteractions(worldState);
  }

  @Test
  public void select_other_player_actionable() {
    when(cell2.player()).thenReturn(Optional.of(otherPlayer));
    when(otherPlayer.canControl()).thenReturn(true);
    when(movements.distanceFrom(cell2)).thenReturn(movement);
    when(stateFactory.createMoving(cell2, movement)).thenReturn(moving);

    moved.select(cell2);

    verify(worldState).rollback();
    verify(worldState).goTo(moving);
    verifyNoMoreInteractions(worldState);
  }

  @Test
  public void actions() {
    when(player.getWeapons()).thenReturn(ImmutableList.of(weapon));
    when(player.getConsumables()).thenReturn(ImmutableList.of(consumable));

    ImmutableList<Action> actions = moved.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(SelectConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}