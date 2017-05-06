package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.item.Weapon;

class SelectWeaponAction implements Action {

  private final PlayerActionState playerActionState;
  private final Weapon weapon;

  SelectWeaponAction(PlayerActionState playerActionState, Weapon weapon) {
    this.playerActionState = playerActionState;
    this.weapon = weapon;
  }

  @Override
  public Message getMessage() {
    return MessageBundle.ITEM.get(weapon.getName());
  }

  @Override
  public String getName() {
    return weapon.getName();
  }

  @Override
  public void run() {
    playerActionState.selectWeapon(weapon);
  }
}
