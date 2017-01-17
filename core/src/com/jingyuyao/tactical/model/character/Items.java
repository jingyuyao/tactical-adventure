package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * A container for character items. Belongs in object package since it is technically part of {@link
 * Character}. <br>
 */
public class Items extends EventBusObject implements Disposable {

  /**
   * Items in the beginning of the list are considered "quick access" items.
   */
  private final List<Item> items;

  @Inject
  Items(EventBus eventBus, @Assisted List<Item> items) {
    super(eventBus);
    this.items = items;
    register();
  }

  @Override
  public void dispose() {
    unregister();
  }

  @Subscribe
  public void removeItem(RemoveItem removeItem) {
    Iterables.removeIf(items, removeItem.getMatchesPredicate());
  }

  Iterable<Weapon> getWeapons() {
    return Iterables.filter(items, Weapon.class);
  }

  Iterable<Consumable> getConsumables() {
    return Iterables.filter(items, Consumable.class);
  }

  Iterable<Item> getItems() {
    return items;
  }

  void quickAccess(Item item) {
    Preconditions.checkArgument(items.contains(item));
    Collections.swap(items, 0, items.indexOf(item));
  }
}
