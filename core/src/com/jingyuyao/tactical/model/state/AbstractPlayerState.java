package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.Locale;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(
      EventBus eventBus, MapState mapState, StateFactory stateFactory, Player player) {
    super(eventBus, mapState, stateFactory);
    this.player = player;
  }

  Player getPlayer() {
    return player;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    // Show the first two weapon and consumable for quick access
    for (Weapon weapon : Iterables.limit(player.getWeapons(), 1)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : Iterables.limit(player.getConsumables(), 1)) {
      builder.add(this.new UseConsumable(consumable));
    }
    if (!Iterables.isEmpty(player.getItems())) {
      builder.add(this.new SelectItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  void finish() {
    player.setActionable(false);
    newWaitStack();
  }

  class SelectWeapon implements Action {

    private final Weapon weapon;

    SelectWeapon(Weapon weapon) {
      this.weapon = weapon;
    }

    @Override
    public String getName() {
      return String.format(Locale.US, "%s (%d)", weapon.getName(), weapon.getUsageLeft());
    }

    @Override
    public void run() {
      player.quickAccess(weapon);
      goTo(getStateFactory()
          .createSelectingTarget(getPlayer(), weapon.createTargets(getPlayer())));
    }
  }

  class UseConsumable implements Action {

    private final Consumable consumable;

    UseConsumable(Consumable consumable) {
      this.consumable = consumable;
    }

    @Override
    public String getName() {
      return String.format(Locale.US, "%s (%d)", consumable.getName(), consumable.getUsageLeft());
    }

    @Override
    public void run() {
      getPlayer().quickAccess(consumable);
      consumable.consume(getPlayer());
      finish();
    }
  }

  class SelectItems implements Action {

    @Override
    public String getName() {
      return "all items";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingItem(player));
    }
  }

  class Wait implements Action {

    @Override
    public String getName() {
      return "wait";
    }

    @Override
    public void run() {
      finish();
    }
  }
}
