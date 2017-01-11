package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
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

  private Items items;

  @Before
  public void setUp() {
    List<Weapon> weapons = Lists.newArrayList(weapon1, weapon2);
    List<Consumable> consumables = Lists.newArrayList(consumable);

    items = new Items(eventBus, weapons, consumables);
    verify(eventBus).register(items);
  }

  @Test
  public void default_weapon() {
    Optional<Weapon> equippedWeapon = items.getEquippedWeapon();
    assertThat(equippedWeapon.isPresent()).isTrue();
    assertThat(equippedWeapon.get()).isSameAs(weapon1);
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
    when(removeItem.matches(weapon1)).thenReturn(true);

    items.removeItem(removeItem);

    assertThat(Iterables.size(items.getWeapons())).isEqualTo(1);
    Optional<Weapon> equippedWeapon = items.getEquippedWeapon();
    assertThat(equippedWeapon.isPresent()).isTrue();
    assertThat(equippedWeapon.get()).isSameAs(weapon2);
  }

  @Test
  public void swap_weapon() {
    items.setEquippedWeapon(weapon2);

    assertThat(items.getWeapons()).containsExactly(weapon2, weapon1).inOrder();
  }

  @Test
  public void get_items() {
    assertThat(items.getItems()).containsExactly(weapon1, weapon2, consumable);
  }

  @Test
  public void subscribers() {
    when(removeItem.getMatchesPredicate()).thenReturn(itemPredicate);

    TestHelpers.verifyNoDeadEvents(items, removeItem);
  }
}