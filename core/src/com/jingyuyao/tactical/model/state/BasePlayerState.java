package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedPlayer;
import com.jingyuyao.tactical.model.event.DeactivatedPlayer;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;

class BasePlayerState extends BaseState {

  private final EventBus eventBus;
  private final StateFactory stateFactory;
  private final Player player;

  BasePlayerState(EventBus eventBus, MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory);
    this.eventBus = eventBus;
    this.stateFactory = stateFactory;
    this.player = player;
  }

  EventBus getEventBus() {
    return eventBus;
  }

  Player getPlayer() {
    return player;
  }

  @Override
  public void enter() {
    eventBus.post(new ActivatedPlayer(player));
  }

  @Override
  public void exit() {
    eventBus.post(new DeactivatedPlayer());
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    FluentIterable<Item> items = getPlayer().fluentItems();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeaponAction(this, stateFactory, getPlayer(), weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new UseConsumableAction(this, getPlayer(), consumable));
    }
    builder.add(new FinishAction(this));
    builder.add(new BackAction(this));
    return builder.build();
  }

  void finish() {
    player.setActionable(false);
    branchToWait();
  }
}
