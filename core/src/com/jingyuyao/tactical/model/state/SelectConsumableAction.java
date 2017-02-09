package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;

class SelectConsumableAction implements Action {

  private final BasePlayerState playerState;
  private final StateFactory stateFactory;
  private final Player player;
  private final Consumable consumable;

  SelectConsumableAction(
      BasePlayerState playerState, StateFactory stateFactory, Player player,
      Consumable consumable) {
    this.playerState = playerState;
    this.stateFactory = stateFactory;
    this.player = player;
    this.consumable = consumable;
  }

  @Override
  public String getText() {
    return consumable.getName();
  }

  @Override
  public void run() {
    player.quickAccess(consumable);
    playerState.goTo(stateFactory.createUsingConsumable(player, consumable));
  }
}
