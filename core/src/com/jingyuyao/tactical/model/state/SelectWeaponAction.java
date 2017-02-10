package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Weapon;

class SelectWeaponAction implements Action {

  private final BasePlayerState playerState;
  private final Weapon weapon;

  SelectWeaponAction(BasePlayerState playerState, Weapon weapon) {
    this.playerState = playerState;
    this.weapon = weapon;
  }

  @Override
  public String getName() {
    return weapon.getName();
  }

  @Override
  public void run() {
    playerState.selectWeapon(weapon);
  }
}
