package com.jingyuyao.tactical.model.character;

public class BasePlayer extends BaseCharacter implements Player {

  private boolean actionable;

  BasePlayer(
      String name, int maxHp, int hp, int moveDistance, Items items, boolean actionable) {
    super(name, maxHp, hp, moveDistance, items);
    this.actionable = actionable;
  }

  @Override
  public boolean isActionable() {
    return actionable;
  }

  @Override
  public void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
