package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedPlayer;
import com.jingyuyao.tactical.model.event.DeactivatedPlayer;
import com.jingyuyao.tactical.model.event.HideTarget;
import com.jingyuyao.tactical.model.event.ShowTarget;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
  private EventBus eventBus;
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
  private Battling battling;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private ImmutableList<Target> targets;
  private SelectingTarget selectingTarget;

  @Before
  public void setUp() {
    targets = ImmutableList.of(target1, target2);
    selectingTarget = new SelectingTarget(eventBus, mapState, stateFactory, player, weapon,
        targets);
  }

  @Test
  public void enter() {
    selectingTarget.enter();

    verify(eventBus, times(4)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(selectingTarget);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, player, ActivatedPlayer.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, target1, ShowTarget.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 3, target2, ShowTarget.class);
  }

  @Test
  public void exit() {
    selectingTarget.exit();

    verify(eventBus, times(3)).post(argumentCaptor.capture());
    TestHelpers.verifyModelEvent(argumentCaptor, 0, DeactivatedPlayer.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, target1, HideTarget.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, target2, HideTarget.class);
  }

  @Test
  public void select_player() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(target1.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createBattling(player, weapon, target1)).thenReturn(battling);

    selectingTarget.select(player);

    verify(mapState).goTo(battling);
  }

  @Test
  public void select_enemy() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(target2.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createBattling(player, weapon, target2)).thenReturn(battling);

    selectingTarget.select(enemy);

    verify(mapState).goTo(battling);
  }

  @Test
  public void select_terrain() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(target2.selectedBy(COORDINATE)).thenReturn(true);
    when(stateFactory.createBattling(player, weapon, target2)).thenReturn(battling);

    selectingTarget.select(terrain);

    verify(mapState).goTo(battling);
  }

  @Test
  public void select_no_select() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(terrain.getCoordinate()).thenReturn(COORDINATE);

    selectingTarget.select(player);
    selectingTarget.select(enemy);
    selectingTarget.select(terrain);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = selectingTarget.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(BackAction.class);
  }
}