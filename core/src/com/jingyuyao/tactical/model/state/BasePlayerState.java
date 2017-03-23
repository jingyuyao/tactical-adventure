package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

class BasePlayerState extends BaseState implements PlayerState {

  private final StateFactory stateFactory;
  private final Player player;

  BasePlayerState(EventBus eventBus, MapState mapState, StateFactory stateFactory, Player player) {
    super(eventBus, mapState);
    this.stateFactory = stateFactory;
    this.player = player;
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    FluentIterable<Item> items = getPlayer().fluentItems();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeaponAction(this, weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new SelectConsumableAction(this, consumable));
    }
    builder.add(new FinishAction(this));
    builder.add(new BackAction(this));
    return builder.build();
  }

  void selectWeapon(Weapon weapon) {
    player.quickAccess(weapon);
    // TODO: fix me!
    ImmutableList<Target> targets = weapon.createTargets(null);
    goTo(stateFactory.createSelectingTarget(player, weapon, targets));
  }

  void selectConsumable(Consumable consumable) {
    player.quickAccess(consumable);
    goTo(stateFactory.createUsingConsumable(player, consumable));
  }

  void finish() {
    player.setActionable(false);
    branchTo(stateFactory.createWaiting());
  }
}
