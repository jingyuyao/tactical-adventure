package com.jingyuyao.tactical.model.character;

public class PlayerData extends CharacterData {

  private boolean actionable;

  public PlayerData(
      String name,
      int maxHp,
      int hp,
      int moveDistance,
      boolean actionable) {
    super(name, maxHp, hp, moveDistance);
    this.actionable = actionable;
  }

  boolean isActionable() {
    return actionable;
  }

  void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
