package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

class ItemActions extends AbstractPlayerState {

  ItemActions(MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    // Show the first weapon and consumable for quick access
    for (Weapon weapon : getPlayer().fluentItems().filter(Weapon.class).limit(1)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : getPlayer().fluentItems().filter(Consumable.class).limit(1)) {
      builder.add(this.new UseConsumable(consumable));
    }
    if (!getPlayer().fluentItems().isEmpty()) {
      builder.add(this.new SelectItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  class SelectItems implements Action {

    @Override
    public String getText() {
      return "all items";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingItem(getPlayer()));
    }
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
