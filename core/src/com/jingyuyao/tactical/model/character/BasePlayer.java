package com.jingyuyao.tactical.model.character;

public class BasePlayer extends AbstractCharacter implements Player {

  private boolean actionable;

  BasePlayer() {
  }

  BasePlayer(
      String name, int maxHp, int hp, int moveDistance, Items items, boolean actionable) {
    super(name, maxHp, hp, moveDistance, items);
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
