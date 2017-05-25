package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

class AttackAction implements Action {

  private final Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
  }

  @Override
  public StringKey getText() {
    return ModelBundle.ACTION.get("attack");
  }

  @Override
  public void run() {
    battling.attack();
  }
}
