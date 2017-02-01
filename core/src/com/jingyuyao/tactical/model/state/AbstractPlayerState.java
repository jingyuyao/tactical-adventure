package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(MapState mapState, StateFactory stateFactory, Player player) {
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

  class SelectWeapon implements Action {

    private final Weapon weapon;

    SelectWeapon(Weapon weapon) {
      this.weapon = weapon;
    }

    @Override
    public String getText() {
      return weapon.toString();
    }

    @Override
    public void run() {
      player.quickAccess(weapon);
      goTo(getStateFactory()
          .createSelectingTarget(player, weapon, weapon.createTargets(player.getCoordinate())));
    }
  }

  class UseConsumable implements Action {

    private final Consumable consumable;

    UseConsumable(Consumable consumable) {
      this.consumable = consumable;
    }

    @Override
    public String getText() {
      return consumable.toString();
    }

    @Override
    public void run() {
      player.quickAccess(consumable);
      consumable.apply(player);
      player.useItem(consumable);
      finish();
    }
  }
}
