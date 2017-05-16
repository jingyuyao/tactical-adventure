package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.ModelBundle;

class AttackAction implements Action {

  private final Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
  }

  @Override
  public Message getMessage() {
    return ModelBundle.ACTION.get("attack");
  }

  @Override
  public void run() {
    battling.attack();
  }
}
