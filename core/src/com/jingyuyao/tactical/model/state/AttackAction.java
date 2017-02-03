package com.jingyuyao.tactical.model.state;

class AttackAction implements Action {

  private Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
  }

  @Override
  public String getText() {
    return "attack";
  }

  @Override
  public void run() {
    battling.attack();
  }
}
