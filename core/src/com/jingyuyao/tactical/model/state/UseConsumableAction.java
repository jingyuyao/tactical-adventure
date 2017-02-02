package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;

class UseConsumableAction implements Action {

  private final AbstractPlayerState playerState;
  private final Player player;
  private final Consumable consumable;

  UseConsumableAction(AbstractPlayerState playerState, Player player, Consumable consumable) {
    this.playerState = playerState;
    this.player = player;
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
    playerState.finish();
  }
}
