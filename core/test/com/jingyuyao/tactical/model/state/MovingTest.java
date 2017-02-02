package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HideMovement;
import com.jingyuyao.tactical.model.event.ShowMovement;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.terrain.Terrain;
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
public class MovingTest {

  private static final Coordinate MOVING_PLAYER_COORDINATE = new Coordinate(0, 1);
  private static final Coordinate TERRAIN_COORDINATE = new Coordinate(0, 2);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private EventBus eventBus;
  @Mock
  private Player player;
  @Mock
  private Player otherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Moved moved;
  @Mock
  private Movement movement;
  @Mock
  private Path path;
  @Mock
  private Transition transition;
  @Mock
  private Moving anotherMoving;
  @Mock
  private Movement otherMovement;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ListenableFuture<Void> immediateFuture;
  private Moving moving;

  @Before
  public void setUp() {
    // Futures are too hard to mock correctly
    immediateFuture = Futures.immediateFuture(null);
    moving = new Moving(mapState, stateFactory, movements, eventBus, player, movement);
  }

  @Test
  public void enter() {
    moving.enter();

    verify(eventBus).post(argumentCaptor.capture());
    ShowMovement showMovement =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), ShowMovement.class);
    assertThat(showMovement.getObject()).isSameAs(movement);
  }

  @Test
  public void canceled_nothing() {
    moving.canceled();

    verifyZeroInteractions(player);
  }

  @Test
  public void canceled_terrain_move() {
    select_terrain_can_move();

    moving.canceled();
    moving.canceled();

    verify(player).instantMoveTo(MOVING_PLAYER_COORDINATE);
  }

  @Test
  public void exit() {
    moving.exit();

    verify(eventBus).post(argumentCaptor.capture());
    HideMovement hideMovement =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), HideMovement.class);
    assertThat(hideMovement.getObject()).isSameAs(movement);
  }

  @Test
  public void select_player() {
    moving.select(player);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(otherPlayer.isActionable()).thenReturn(false);

    moving.select(otherPlayer);

    verify(mapState).rollback();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_actionable() {
    when(otherPlayer.isActionable()).thenReturn(true);
    when(movements.distanceFrom(otherPlayer)).thenReturn(otherMovement);
    when(stateFactory.createMoving(otherPlayer, otherMovement)).thenReturn(anotherMoving);

    moving.select(otherPlayer);

    verify(mapState).rollback();
    verify(mapState).goTo(anotherMoving);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_enemy() {
    moving.select(enemy);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_can_move() {
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(true);
    when(movement.pathTo(TERRAIN_COORDINATE)).thenReturn(path);
    when(player.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createMoved(player)).thenReturn(moved);
    when(stateFactory.createTransition()).thenReturn(transition);
    when(player.moveAlong(path)).thenReturn(immediateFuture);

    moving.select(terrain);

    InOrder inOrder = Mockito.inOrder(player, mapState);
    inOrder.verify(mapState).goTo(transition);
    inOrder.verify(player).moveAlong(path);
    inOrder.verify(mapState).goTo(moved);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_cannot_move() {
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(false);

    moving.select(terrain);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions() {
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));

    ImmutableList<Action> actions = moving.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(UseConsumableAction.class);
    assertThat(actions.get(2)).isInstanceOf(FinishAction.class);
    assertThat(actions.get(3)).isInstanceOf(BackAction.class);
  }
}
