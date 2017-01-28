package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.terrain.Terrain.Type;
import java.util.Set;

/**
 * Setters should be package private.
 */
public class CharacterData {

  private final String name;
  private final Set<Terrain.Type> passableTerrainTypes;
  private int maxHp;
  /**
   * 0 <= hp <= maxHp
   */
  private int hp;

  private int moveDistance;

  public CharacterData(
      String name,
      int maxHp,
      int hp,
      int moveDistance,
      Set<Type> passableTerrainTypes) {
    Preconditions.checkArgument(hp >= 0 && hp <= maxHp);
    this.name = name;
    this.maxHp = maxHp;
    this.hp = hp;
    this.passableTerrainTypes = passableTerrainTypes;
    this.moveDistance = moveDistance;
  }

  String getName() {
    return name;
  }

  int getHp() {
    return hp;
  }

  int getMoveDistance() {
    return moveDistance;
  }

  void damageBy(int delta) {
    hp = Math.max(hp - delta, 0);
  }

  void healBy(int delta) {
    hp = Math.min(hp + delta, maxHp);
  }

  boolean isDead() {
    return hp == 0;
  }

  boolean canPassTerrainType(Terrain.Type terrainType) {
    return passableTerrainTypes.contains(terrainType);
  }
}
