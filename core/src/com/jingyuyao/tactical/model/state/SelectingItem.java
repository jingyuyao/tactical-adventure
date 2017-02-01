package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import javax.inject.Inject;

// TODO: Fire off an event so the UI show a widget that manages items
class SelectingItem extends AbstractPlayerState {

  @Inject
  SelectingItem(MapState mapState, StateFactory stateFactory, @Assisted Player player) {
    super(mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    for (Weapon weapon : getPlayer().fluentItems().filter(Weapon.class)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : getPlayer().fluentItems().filter(Consumable.class)) {
      builder.add(this.new UseConsumable(consumable));
    }
    builder.add(this.new Back());
    return builder.build();
  }
}
