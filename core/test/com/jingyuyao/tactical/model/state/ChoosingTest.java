package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChoosingTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player choosingPlayer;
  @Mock
  private Player anotherPlayer;
  @Mock
  private Moving moving;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private SelectingWeapon selectingWeapon;
  @Mock
  private UsingItem usingItem;
  @Mock
  private Waiting waiting;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;

  private Iterable<Weapon> weaponIterable;
  private Iterable<Consumable> consumableIterable;
  private Choosing choosing;

  @Before
  public void setUp() {
    weaponIterable = ImmutableList.of(weapon);
    consumableIterable = ImmutableList.of(consumable);
    choosing = new Choosing(eventBus, mapState, stateFactory, choosingPlayer);
  }

  @Test
  public void select_same_player() {
    choosing.select(choosingPlayer);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_different_player() {
    when(stateFactory.createMoving(anotherPlayer)).thenReturn(moving);

    choosing.select(anotherPlayer);

    InOrder inOrder = Mockito.inOrder(mapState);
    inOrder.verify(mapState).rollback();
    inOrder.verify(mapState).push(moving);
  }

  @Test
  public void select_enemy() {
    choosing.select(enemy);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain() {
    choosing.select(terrain);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = actions_set_up();

    Action selectWeapons = actions.get(0);
    selectWeapons.run();
    verify(mapState).push(selectingWeapon);

    Action useItems = actions.get(1);
    useItems.run();
    verify(mapState).push(usingItem);

    Action wait = actions.get(2);
    wait.run();
    verify(choosingPlayer).setActionable(false);
    verify(mapState).newStack(waiting);

    StateHelpers.verifyBack(actions.get(3), mapState);
  }

  private ImmutableList<Action> actions_set_up() {
    when(choosingPlayer.getWeapons()).thenReturn(weaponIterable);
    when(choosingPlayer.getConsumables()).thenReturn(consumableIterable);
    when(stateFactory.createSelectingWeapon(choosingPlayer)).thenReturn(selectingWeapon);
    when(stateFactory.createUsingItem(choosingPlayer)).thenReturn(usingItem);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = choosing.getActions();
    assertThat(actions).hasSize(4);
    return actions;
  }
}