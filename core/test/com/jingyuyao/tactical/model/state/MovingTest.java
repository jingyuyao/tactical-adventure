package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Targets.FilteredTargets;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingTest {

  private static final Coordinate MOVING_PLAYER_COORDINATE = new Coordinate(0, 1);
  private static final Coordinate ENEMY_COORDINATE = new Coordinate(0, 0);
  private static final Coordinate TERRAIN_COORDINATE = new Coordinate(0, 2);

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player movingPlayer;
  @Mock
  private Player anotherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Choosing choosing;
  @Mock
  private Moving dummyMoving;
  @Mock
  private SelectingWeapon selectingWeapon;
  @Mock
  private Targets targets;
  @Mock
  private Path path;
  @Mock
  private FilteredTargets allTargets;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private ListenableFuture<Void> immediateFuture;
  private Moving moving;

  @Before
  public void setUp() {
    // Futures are too hard to mock correctly
    immediateFuture = Futures.immediateFuture(null);
    moving = new Moving(eventBus, mapState, stateFactory, movingPlayer);
  }

  @Test
  public void enter() {
    moving.enter();

    verify(movingPlayer).showAllTargetsWithMove();
  }

  @Test
  public void canceled_nothing() {
    moving.canceled();

    verifyZeroInteractions(movingPlayer);
  }

  @Test
  public void canceled_enemy_move() {
    select_enemy_can_hit();

    moving.canceled();

    verify(movingPlayer).createTargets();
    verify(movingPlayer).getCoordinate();
    verify(movingPlayer).instantMoveTo(MOVING_PLAYER_COORDINATE);

    moving.canceled();

    verifyNoMoreInteractions(movingPlayer);
  }

  @Test
  public void canceled_terrain_move() {
    select_terrain_can_move();

    moving.canceled();

    verify(movingPlayer).createTargets();
    verify(movingPlayer).getCoordinate();
    verify(movingPlayer).instantMoveTo(MOVING_PLAYER_COORDINATE);

    moving.canceled();

    verifyNoMoreInteractions(movingPlayer);
  }

  @Test
  public void exit() {
    moving.exit();

    verify(movingPlayer).clearMarking();
  }

  @Test
  public void select_same_player() {
    when(stateFactory.createChoosing(movingPlayer)).thenReturn(choosing);

    moving.select(movingPlayer);

    verify(stateFactory).createChoosing(movingPlayer);
    verify(mapState).push(choosing);
  }

  @Test
  public void select_differentPlayer() {
    when(stateFactory.createMoving(anotherPlayer)).thenReturn(dummyMoving);

    moving.select(anotherPlayer);

    verify(stateFactory).createMoving(anotherPlayer);
    verify(mapState).rollback();
    verify(mapState).push(dummyMoving);
  }

  @Test
  public void select_enemy_can_hit() {
    when(movingPlayer.createTargets()).thenReturn(targets);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.canTarget(enemy)).thenReturn(true);
    when(enemy.getCoordinate()).thenReturn(ENEMY_COORDINATE);
    when(targets.movePathToTargetCoordinate(ENEMY_COORDINATE)).thenReturn(path);
    when(movingPlayer.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createChoosing(movingPlayer)).thenReturn(choosing);
    when(stateFactory.createSelectingWeapon(movingPlayer, enemy)).thenReturn(selectingWeapon);
    when(movingPlayer.move(path)).thenReturn(immediateFuture);

    moving.select(enemy);

    verify(movingPlayer).move(path);
    verify(mapState).push(choosing);
    verify(mapState).push(selectingWeapon);
  }

  @Test
  public void select_enemy_cannot_hit() {
    when(movingPlayer.createTargets()).thenReturn(targets);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.canTarget(enemy)).thenReturn(false);

    moving.select(enemy);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_can_move() {
    when(movingPlayer.createTargets()).thenReturn(targets);
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(targets.canMoveTo(TERRAIN_COORDINATE)).thenReturn(true);
    when(targets.pathTo(TERRAIN_COORDINATE)).thenReturn(path);
    when(movingPlayer.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createChoosing(movingPlayer)).thenReturn(choosing);
    when(movingPlayer.move(path)).thenReturn(immediateFuture);

    moving.select(terrain);

    verify(movingPlayer).getCoordinate();
    verify(movingPlayer).createTargets();
    verify(movingPlayer).move(path);
    verify(mapState).push(choosing);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_cannot_move() {
    when(movingPlayer.createTargets()).thenReturn(targets);
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(targets.canMoveTo(TERRAIN_COORDINATE)).thenReturn(false);

    moving.select(terrain);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_has_moved() {
    select_terrain_can_move();

    moving.select(enemy);
    moving.select(movingPlayer);
    moving.select(terrain);

    verifyNoMoreInteractions(movingPlayer);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = moving.getActions();
    assertThat(actions).hasSize(1);
    StateHelpers.verifyBack(actions.get(0), mapState);
  }

  @Test
  public void action_back_moved() {
    select_terrain_can_move();

    ImmutableList<Action> actions = moving.getActions();
    assertThat(actions).hasSize(1);
    actions.get(0).run();

    verifyZeroInteractions(mapState);
  }
}
