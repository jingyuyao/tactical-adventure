package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;

class ItemActions extends AbstractPlayerState {

  ItemActions(MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    FluentIterable<Item> items = getPlayer().fluentItems();
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(this.new UseConsumable(consumable));
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  class Wait implements Action {

    @Override
    public String getText() {
      return "wait";
    }

    @Override
    public void run() {
      finish();
    }
  }
}
