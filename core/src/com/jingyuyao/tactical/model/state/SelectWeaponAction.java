package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class SelectWeaponAction implements Action {

  private final PlayerActionState playerActionState;
  private final Weapon weapon;

  SelectWeaponAction(PlayerActionState playerActionState, Weapon weapon) {
    this.playerActionState = playerActionState;
    this.weapon = weapon;
  }

  @Override
  public ResourceKey getText() {
    return weapon.getName();
  }

  @Override
  public void run() {
    playerActionState.selectWeapon(weapon);
  }
}
