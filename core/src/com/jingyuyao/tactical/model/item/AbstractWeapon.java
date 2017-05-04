package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Locale;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
abstract class AbstractWeapon extends AbstractItem implements Weapon {

  private int attackPower;

  AbstractWeapon() {
  }

  AbstractWeapon(int attackPower) {
    this.attackPower = attackPower;
  }

  @Override
  public String getDescription() {
    return String.format(Locale.US, "%d attack weapon", attackPower);
  }

  /**
   * Apply pre-damage, damage, and post-damage effects to each targeted cell.
   */
  @Override
  public void apply(Character attacker, Target target) {
    for (Cell cell : target.getTargetCells()) {
      preDamage(attacker, cell);
      damage(attacker, cell);
      postDamage(attacker, cell);
    }
  }

  /**
   * Default implementation damage the character in the cell if there any.
   * Override me to change effects per cell.
   */
  void damage(Character attacker, Cell cell) {
    for (Character defender : cell.character().asSet()) {
      defender.damageBy(getDamage(attacker, cell));
    }
  }

  /**
   * Default implementation does nothing.
   * Override me to add pre-damage effects.
   */
  void preDamage(Character attacker, Cell cell) {
  }

  /**
   * Default implementation does nothing.
   * Override me to add post-damage effects.
   */
  void postDamage(Character attacker, Cell cell) {
  }

  int getDamage(Character attacker, Cell cell) {
    for (Character defender : cell.character().asSet()) {
      return Math.max(attackPower - defender.getDefense(), 0);
    }
    return attackPower;
  }
}
