package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

class AttackAction implements Action {

  private final Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
  }

  @Override
  public Message getMessage() {
    return MessageBundle.ACTION.get("attack");
  }

  @Override
  public String getName() {
    return "attack";
  }

  @Override
  public void run() {
    battling.attack();
  }
}
