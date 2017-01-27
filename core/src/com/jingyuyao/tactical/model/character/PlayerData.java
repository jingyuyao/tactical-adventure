package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.map.Terrain.Type;
import java.util.Set;

public class PlayerData extends CharacterData {

  private boolean actionable;

  public PlayerData(
      String name,
      int maxHp,
      int hp,
      int moveDistance,
      Set<Type> passableTerrainTypes,
      boolean actionable) {
    super(name, maxHp, hp, moveDistance, passableTerrainTypes);
    this.actionable = actionable;
  }

  boolean isActionable() {
    return actionable;
  }

  void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
