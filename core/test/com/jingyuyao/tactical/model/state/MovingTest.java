package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingTest extends AbstractStateTest {

  private static final Coordinate MOVING_PLAYER_COORDINATE = new Coordinate(0, 1);
  private static final Coordinate ENEMY_COORDINATE = new Coordinate(0, 0);
  private static final Coordinate TERRAIN_COORDINATE = new Coordinate(0, 2);

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Markings markings;
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

  private Moving moving;

  @Before
  public void setUp() {
    moving = new Moving(eventBus, mapState, markings, stateFactory, movingPlayer);
  }

  @Test
  public void enter() {
    moving.enter();

    markings.showMoveAndTargets(movingPlayer);
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

    verify(movingPlayer).instantMoveTo(MOVING_PLAYER_COORDINATE);
  }

  @Test
  public void canceled_terrain_move() {
    select_terrain_can_move();

    moving.canceled();

    verify(movingPlayer).instantMoveTo(MOVING_PLAYER_COORDINATE);
  }

  @Test
  public void exit() {
    moving.exit();

    markings.clearPlayerMarking();
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
    when(targets.canHitAfterMove(enemy)).thenReturn(true);
    when(enemy.getCoordinate()).thenReturn(ENEMY_COORDINATE);
    when(targets.movePathToTarget(ENEMY_COORDINATE)).thenReturn(path);
    when(movingPlayer.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createChoosing(movingPlayer)).thenReturn(choosing);
    when(stateFactory.createSelectingWeapon(movingPlayer, enemy)).thenReturn(selectingWeapon);

    moving.select(enemy);

    verify(movingPlayer).move(path);
    verify(mapState).push(choosing);
    verify(mapState).push(selectingWeapon);
  }

  @Test
  public void select_enemy_cannot_hit() {
    when(movingPlayer.createTargets()).thenReturn(targets);
    when(targets.canHitAfterMove(enemy)).thenReturn(false);

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

    moving.select(terrain);

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
  public void actions() {
    ImmutableList<Action> actions = moving.getActions();
    assertThat(actions).hasSize(1);
    verifyBack(actions.get(0), mapState);
  }
}
