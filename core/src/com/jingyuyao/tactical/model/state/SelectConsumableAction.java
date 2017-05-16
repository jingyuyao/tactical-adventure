package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.resource.Message;

class SelectConsumableAction implements Action {

  private final PlayerActionState playerActionState;
  private final Consumable consumable;

  SelectConsumableAction(PlayerActionState playerActionState, Consumable consumable) {
    this.playerActionState = playerActionState;
    this.consumable = consumable;
  }

  @Override
  public Message getMessage() {
    return consumable.getName();
  }

  @Override
  public void run() {
    playerActionState.selectConsumable(consumable);
  }
}
