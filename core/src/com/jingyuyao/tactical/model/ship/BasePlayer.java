package com.jingyuyao.tactical.model.ship;

public class BasePlayer extends BaseShip implements Player {

  private boolean controllable;

  BasePlayer() {
  }

  BasePlayer(String nameKey, boolean controllable, Stats stats, Cockpit cockpit, Items items) {
    super(nameKey, stats, cockpit, items);
    this.controllable = controllable;
  }

  @Override
  public boolean isControllable() {
    return controllable;
  }

  @Override
  public void setControllable(boolean controllable) {
    this.controllable = controllable;
  }
}
