package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

class SelectWeaponAction implements Action {

  private final AbstractPlayerState playerState;
  private final Weapon weapon;

  SelectWeaponAction(AbstractPlayerState playerState, Weapon weapon) {
    this.playerState = playerState;
    this.weapon = weapon;
  }

  @Override
  public String getText() {
    return weapon.toString();
  }

  @Override
  public void run() {
    Player player = playerState.getPlayer();
    player.quickAccess(weapon);
    ImmutableList<Target> targets = weapon.createTargets(player.getCoordinate());
    playerState
        .goTo(playerState.getStateFactory().createSelectingTarget(player, weapon, targets));
  }
}
