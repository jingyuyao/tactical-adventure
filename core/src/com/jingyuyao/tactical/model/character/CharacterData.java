package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;

/**
 * Setters should be package private.
 */
public class CharacterData {

  private String name;
  private int maxHp;
  /**
   * 0 <= hp <= maxHp
   */
  private int hp;
  private int moveDistance;

  // No args constructor is needed for serialization
  CharacterData() {

  }

  public CharacterData(
      String name,
      int maxHp,
      int hp,
      int moveDistance) {
    Preconditions.checkArgument(hp >= 0 && hp <= maxHp);
    this.name = name;
    this.maxHp = maxHp;
    this.hp = hp;
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
}
