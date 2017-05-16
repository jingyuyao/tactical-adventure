package com.jingyuyao.tactical.model.ship;

public class BasePlayer extends BaseShip implements Player {

  private boolean actionable;

  BasePlayer() {
  }

  BasePlayer(String nameKey, boolean actionable, Stats stats, Cockpit cockpit, Items items) {
    super(nameKey, stats, cockpit, items);
    this.actionable = actionable;
  }

  @Override
  public boolean canControl() {
    return actionable;
  }

  @Override
  public void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
