package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
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
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private EventBus eventBus;
  @Mock
  private Movements movements;
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
    moved = new Moved(eventBus, mapState, stateFactory, movements, player);
  }

  @Test
  public void enter() {
    moved.enter();

    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(moved);
  }

  @Test
  public void exit() {
    moved.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, moved, ExitState.class);
  }

  @Test
  public void select_player() {
    moved.select(player);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(otherPlayer.isActionable()).thenReturn(false);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_actionable() {
    when(otherPlayer.isActionable()).thenReturn(true);
    when(movements.distanceFrom(otherPlayer)).thenReturn(movement);
    when(stateFactory.createMoving(otherPlayer, movement)).thenReturn(moving);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verify(mapState).goTo(moving);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions() {
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));

    ImmutableList<Action> actions = moved.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(SelectConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}