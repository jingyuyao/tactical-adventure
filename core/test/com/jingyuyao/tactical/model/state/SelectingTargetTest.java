package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectingTargetTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 1);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Weapon weapon;
  @Mock
  private Target target1;
  @Mock
  private Target target2;
  @Mock
  private ReviewingAttack reviewingAttack;

  private ImmutableList<Target> targets;
  private SelectingTarget selectingTarget;

  @Before
  public void setUp() {
    targets = ImmutableList.of(target1, target2);
    selectingTarget = new SelectingTarget(mapState, stateFactory, player, weapon, targets);
  }

  @Test
  public void enter() {
    selectingTarget.enter();

    target1.showMarking();
    target2.showMarking();
  }

  @Test
  public void exit() {
    selectingTarget.exit();

    target1.hideMarking();
    target2.hideMarking();
  }

  @Test
  public void select_player() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(target1.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createReviewingAttack(player, weapon, target1)).thenReturn(reviewingAttack);

    selectingTarget.select(player);

    verify(mapState).goTo(reviewingAttack);
  }

  @Test
  public void select_enemy() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target2.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createReviewingAttack(player, weapon, target2)).thenReturn(reviewingAttack);

    selectingTarget.select(enemy);

    verify(mapState).goTo(reviewingAttack);
  }

  @Test
  public void select_terrain() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target2.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createReviewingAttack(player, weapon, target2)).thenReturn(reviewingAttack);

    selectingTarget.select(terrain);

    verify(mapState).goTo(reviewingAttack);
  }

  @Test
  public void select_no_select() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(terrain.getCoordinate()).thenReturn(COORDINATE);

    selectingTarget.select(player);
    selectingTarget.select(enemy);
    selectingTarget.select(terrain);

    verify(mapState, times(3)).back();
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = selectingTarget.getActions();
    assertThat(actions).hasSize(1);

    actions.get(0).run();

    verify(mapState).back();
  }
}