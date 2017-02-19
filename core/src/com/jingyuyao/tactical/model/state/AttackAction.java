package com.jingyuyao.tactical.model.state;

class AttackAction implements Action {

  private final Battling battling;

  AttackAction(Battling battling) {
    this.battling = battling;
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
