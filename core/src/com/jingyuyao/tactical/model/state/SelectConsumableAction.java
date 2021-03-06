package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.resource.StringKey;

class SelectConsumableAction implements Action {

  private final ControllingActionState playerActionState;
  private final Consumable consumable;

  SelectConsumableAction(ControllingActionState playerActionState, Consumable consumable) {
    this.playerActionState = playerActionState;
    this.consumable = consumable;
  }

  @Override
  public StringKey getText() {
    return consumable.getName();
  }

  @Override
  public void run() {
    playerActionState.selectConsumable(consumable);
  }
}
