package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;

class BasePlayerState extends BaseState {

  private final Player player;

  BasePlayerState(MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory);
    this.player = player;
  }

  Player getPlayer() {
    return player;
  }

  void finish() {
    player.setActionable(false);
    branchToWait();
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    FluentIterable<Item> items = getPlayer().fluentItems();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeaponAction(this, getStateFactory(), getPlayer(), weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new UseConsumableAction(this, getPlayer(), consumable));
    }
    builder.add(new FinishAction(this));
    builder.add(new BackAction(this));
    return builder.build();
  }
}
