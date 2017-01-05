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
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;
import java.util.Iterator;
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
  private Markings markings;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player choosingPlayer;
  @Mock
  private Player anotherPlayer;
  @Mock
  private Moving moving;
  @Mock
  private Targets targets;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private SelectingWeapon selectingWeapon;
  @Mock
  private Iterable<Consumable> consumables;
  @Mock
  private Iterator<Consumable> consumableIterator;
  @Mock
  private UsingItem usingItem;
  @Mock
  private Waiting waiting;

  private Choosing choosing;

  @Before
  public void setUp() {
    choosing = new Choosing(eventBus, mapState, markings, stateFactory, choosingPlayer);
  }

  @Test
  public void enter() {
    choosing.enter();

    verify(markings).showImmediateTargets(choosingPlayer);
  }

  @Test
  public void exit() {
    choosing.exit();

    verify(markings).clearPlayerMarking();
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
  public void select_enemy_can_hit() {
    when(choosingPlayer.createTargets()).thenReturn(targets);
    when(targets.canHitImmediately(enemy)).thenReturn(true);
    when(stateFactory.createSelectingWeapon(choosingPlayer, enemy)).thenReturn(selectingWeapon);

    choosing.select(enemy);

    verify(stateFactory).createSelectingWeapon(choosingPlayer, enemy);
    verify(mapState).push(selectingWeapon);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_enemy_cannot_hit() {
    when(choosingPlayer.createTargets()).thenReturn(targets);
    when(targets.canHitImmediately(enemy)).thenReturn(false);

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
  public void actions_with_consumable() {
    when(choosingPlayer.getConsumables()).thenReturn(consumables);
    when(consumables.iterator()).thenReturn(consumableIterator);
    when(consumableIterator.hasNext()).thenReturn(true);
    when(stateFactory.createUsingItem(choosingPlayer)).thenReturn(usingItem);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    ImmutableList<Action> actions = choosing.getActions();
    assertThat(actions).hasSize(3);

    Action useItems = actions.get(0);
    useItems.run();
    verify(mapState).push(usingItem);

    verifyWait(actions.get(1));
    StateHelpers.verifyBack(actions.get(2), mapState);
  }

  @Test
  public void actions_without_consumable() {
    when(choosingPlayer.getConsumables()).thenReturn(consumables);
    when(consumables.iterator()).thenReturn(consumableIterator);
    when(consumableIterator.hasNext()).thenReturn(false);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    ImmutableList<Action> actions = choosing.getActions();
    assertThat(actions).hasSize(2);

    verifyWait(actions.get(0));
    StateHelpers.verifyBack(actions.get(1), mapState);
  }

  private void verifyWait(Action wait) {
    wait.run();
    verify(choosingPlayer).setActionable(false);
    verify(mapState).newStack(waiting);
  }
}