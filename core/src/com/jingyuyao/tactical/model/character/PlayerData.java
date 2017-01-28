package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.map.Coordinate;

public class PlayerData extends CharacterData {

  private boolean actionable;

  PlayerData() {
  }

  public PlayerData(
      Coordinate coordinate,
      String name,
      int maxHp,
      int hp,
      int moveDistance,
      boolean actionable) {
    super(coordinate, name, maxHp, hp, moveDistance);
    this.actionable = actionable;
  }

  boolean isActionable() {
    return actionable;
  }

  void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
