package com.jingyuyao.tactical.model.character;

public class BasePlayer extends BaseCharacter implements Player {

  private boolean actionable;

  BasePlayer() {
  }

  BasePlayer(
      String nameKey, String resourceKey, int maxHp, int hp, int moveDistance, Items items,
      boolean actionable) {
    super(nameKey, resourceKey, maxHp, hp, moveDistance, items);
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
