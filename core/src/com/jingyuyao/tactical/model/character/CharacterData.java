package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.MapObjectData;
import java.util.List;

/**
 * Setters should be package private.
 */
public abstract class CharacterData extends MapObjectData {

  private String name;
  private int maxHp;
  /**
   * 0 <= hp <= maxHp
   */
  private int hp;
  private int moveDistance;

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

  /**
   * Enables the visitor pattern for character creation.
   */
  public abstract Character load(CharacterFactory factory, List<Item> items);
}
