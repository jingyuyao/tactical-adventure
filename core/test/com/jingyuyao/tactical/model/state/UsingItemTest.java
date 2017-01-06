package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.map.Terrain;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsingItemTest {

  @Mock
  private EventBus eventBus;
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
  private Iterable<Consumable> consumables;
  @Mock
  private Iterator<Consumable> consumableIterator;
  @Mock
  private Consumable consumable1;
  @Mock
  private Waiting waiting;

  private UsingItem usingItem;

  @Before
  public void setUp() {
    usingItem = new UsingItem(eventBus, mapState, stateFactory, player);
  }

  @Test
  public void select_player() {
    usingItem.select(player);

    verify(mapState).pop();
  }

  @Test
  public void select_enemy() {
    usingItem.select(enemy);

    verify(mapState).pop();
  }

  @Test
  public void select_terrain() {
    usingItem.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void actions_use_item() {
    ImmutableList<Action> actions = actionSetUp();

    Action useConsumable = actions.get(0);
    useConsumable.run();
    verify(consumable1).consume(player);
    verify(player).setActionable(false);
    verify(mapState).newStack(waiting);
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = actionSetUp();

    StateHelpers.verifyBack(actions.get(1), mapState);
  }

  private ImmutableList<Action> actionSetUp() {
    when(player.getConsumables()).thenReturn(consumables);
    when(consumables.iterator()).thenReturn(consumableIterator);
    when(consumableIterator.hasNext()).thenReturn(true, false);
    when(consumableIterator.next()).thenReturn(consumable1);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = usingItem.getActions();
    assertThat(actions).hasSize(2);
    return actions;
  }
}