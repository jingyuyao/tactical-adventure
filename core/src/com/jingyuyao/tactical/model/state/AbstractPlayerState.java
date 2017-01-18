package com.jingyuyao.tactical.model.state;

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
