package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting.EndTurn;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(1, 0);
  private static final String NAME = "me";

  @Mock
  private EventBus eventBus;
  @Mock
  private Stats stats;
  @Mock
  private MapState mapState;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Path path;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Consumable consumable;
  @Mock
  private RemoveItem removeItem;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private List<Item> items;
  private Player player;

  @Before
  public void setUp() {
    items = Lists.newArrayList(weapon1, consumable, weapon2);
    player = new Player(eventBus, COORDINATE, stats, items);
    verify(eventBus).register(player);
    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void dispose() {
    player.dispose();

    verify(eventBus).unregister(player);
  }

  @Test
  public void select() {
    player.select(mapState);

    verify(mapState).select(player);
  }

  @Test
  public void highlight() {
    player.highlight(mapState);

    verify(mapState).highlight(player);
  }

  @Test
  public void get_items() {
    assertThat(player.getItems()).containsExactly(weapon1, consumable, weapon2).inOrder();
    assertThat(player.getWeapons()).containsExactly(weapon1, weapon2).inOrder();
    assertThat(player.getConsumables()).containsExactly(consumable);
  }

  @Test
  public void remove_item() {
    when(removeItem.getObject()).thenReturn(weapon1);

    player.removeItem(removeItem);

    assertThat(items).containsExactly(consumable, weapon2).inOrder();
  }

  @Test
  public void quick_access() {
    player.quickAccess(weapon2);

    assertThat(items).containsExactly(weapon2, consumable, weapon1).inOrder();
  }

  @Test
  public void move() {
    when(path.getDestination()).thenReturn(COORDINATE2);

    ListenableFuture<Void> future = player.moveAlong(path);

    verify(eventBus).post(argumentCaptor.capture());
    Move move = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Move.class);
    assertThat(move.getPath()).isSameAs(path);
    assertThat(future.isDone()).isFalse();

    move.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void name() {
    when(stats.getName()).thenReturn(NAME);

    assertThat(player.getName()).isEqualTo(NAME);
  }

  @Test
  public void set_actionable() {
    player.setActionable(false);

    verify(eventBus).post(argumentCaptor.capture());
    NewActionState newActionState =
        TestHelpers.isInstanceOf(argumentCaptor.getValue(), NewActionState.class);
    assertThat(newActionState.getObject()).isSameAs(player);
    assertThat(newActionState.isActionable()).isFalse();
  }
}