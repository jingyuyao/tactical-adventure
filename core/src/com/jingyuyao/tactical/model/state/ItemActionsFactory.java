package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import javax.inject.Singleton;

@Singleton
class ItemActionsFactory {

  ImmutableList<Action> create(AbstractPlayerState playerState) {
    FluentIterable<Item> items = playerState.getPlayer().fluentItems();
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeaponAction(playerState, weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new UseConsumableAction(playerState, consumable));
    }
    return builder.build();
  }
}
