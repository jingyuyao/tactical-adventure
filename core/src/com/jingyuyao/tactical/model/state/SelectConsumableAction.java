package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Consumable;

class SelectConsumableAction implements Action {

  private final BasePlayerState playerState;
  private final Consumable consumable;

  SelectConsumableAction(BasePlayerState playerState, Consumable consumable) {
    this.playerState = playerState;
    this.consumable = consumable;
  }

  @Override
  public String getName() {
    return consumable.getName();
  }

  @Override
  public void run() {
    playerState.selectConsumable(consumable);
  }
}
