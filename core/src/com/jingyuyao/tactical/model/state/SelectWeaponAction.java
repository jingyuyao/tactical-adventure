package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

class SelectWeaponAction implements Action {

  private final AbstractPlayerState playerState;
  private final StateFactory stateFactory;
  private final Player player;
  private final Weapon weapon;

  SelectWeaponAction(
      AbstractPlayerState playerState, StateFactory stateFactory, Player player, Weapon weapon) {
    this.playerState = playerState;
    this.stateFactory = stateFactory;
    this.player = player;
    this.weapon = weapon;
  }

  @Override
  public String getText() {
    return weapon.toString();
  }

  @Override
  public void run() {
    player.quickAccess(weapon);
    ImmutableList<Target> targets = weapon.createTargets(player.getCoordinate());
    playerState.goTo(stateFactory.createSelectingTarget(player, weapon, targets));
  }
}
