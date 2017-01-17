package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemsTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Consumable consumable;
  @Mock
  private RemoveItem removeItem;
  @Mock
  private Predicate<Item> itemPredicate;

  private List<Item> itemIterable;
  private Items items;

  @Before
  public void setUp() {
    itemIterable = Lists.<Item>newArrayList(weapon1, consumable, weapon2);

    items = new Items(eventBus, itemIterable);
    verify(eventBus).register(items);
  }

  @Test
  public void dispose() {
    items.dispose();

    verify(eventBus).unregister(items);
  }

  @Test
  public void remove_item() {
    when(removeItem.getMatchesPredicate()).thenReturn(itemPredicate);
    when(itemPredicate.apply(weapon1)).thenReturn(true);

    items.removeItem(removeItem);

    assertThat(Iterables.size(items.getWeapons())).isEqualTo(1);
  }

  @Test
  public void quick_access() {
    items.quickAccess(weapon2);

    assertThat(items.getItems()).containsExactly(weapon2, consumable, weapon1).inOrder();
  }

  @Test
  public void get_items() {
    assertThat(items.getItems()).containsExactly(weapon1, consumable, weapon2);
  }

  @Test
  public void subscribers() {
    when(removeItem.getMatchesPredicate()).thenReturn(itemPredicate);

    TestHelpers.verifyNoDeadEvents(items, removeItem);
  }
}