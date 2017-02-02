package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovedTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private Player player;
  @Mock
  private Player otherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Movement movement;
  @Mock
  private Moving moving;
  @Mock
  private Waiting waiting;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;

  private Moved moved;

  @Before
  public void setUp() {
    moved = new Moved(mapState, stateFactory, movements, player);
  }

  @Test
  public void select_player() {
    moved.select(player);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
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
  public void select_enemy() {
    moved.select(enemy);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain() {
    moved.select(terrain);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions_from_factory() {
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));

    ImmutableList<Action> actions = moved.getActions();

    assertThat(actions).hasSize(4);
    assertThat(actions.get(0)).isInstanceOf(SelectWeaponAction.class);
    assertThat(actions.get(1)).isInstanceOf(UseConsumableAction.class);
  }

  @Test
  public void action_finish() {
    when(player.fluentItems()).thenReturn(FluentIterable.<Item>of());
    when(stateFactory.createWaiting()).thenReturn(waiting);

    ImmutableList<Action> actions = moved.getActions();

    assertThat(actions).hasSize(2);

    actions.get(0).run();

    verify(player).setActionable(false);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void action_back() {
    when(player.fluentItems()).thenReturn(FluentIterable.<Item>of());

    ImmutableList<Action> actions = moved.getActions();

    assertThat(actions).hasSize(2);

    actions.get(1).run();

    verify(mapState).back();
  }
}