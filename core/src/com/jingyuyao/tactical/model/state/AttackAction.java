package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class AttackAction implements Action {

  private final Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
  }

  @Override
  public ResourceKey getText() {
    return ModelBundle.ACTION.get("attack");
  }

  @Override
  public void run() {
    battling.attack();
  }
}
