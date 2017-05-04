package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
class BaseWeapon extends BaseItem implements Weapon {

  private int attackPower;
  private boolean lifeSteal;
  private float lifeStealRate;
  private boolean recoil;
  private float recoilRate;

  BaseWeapon() {
  }

  BaseWeapon(
      int attackPower,
      boolean lifeSteal,
      float lifeStealRate,
      boolean recoil,
      float recoilRate) {
    this.attackPower = attackPower;
    this.lifeSteal = lifeSteal;
    this.lifeStealRate = lifeStealRate;
    this.recoil = recoil;
    this.recoilRate = recoilRate;
  }

  @Override
  public String getDescription() {
    StringBuilder builder = new StringBuilder();
    builder.append(attackPower).append(" attack");
    if (lifeSteal) {
      builder.append(", ").append((int) (lifeStealRate * 100)).append("% lifesteal");
    }
    if (recoil) {
      builder.append(", ").append((int) (recoilRate * 100)).append("% recoil");
    }
    return builder.toString();
  }

  /**
   * Apply pre-damage, damage, and post-damage effects to each targeted cell.
   */
  @Override
  public void apply(Character attacker, Target target) {
    for (Cell cell : target.getTargetCells()) {
      int damage = getDamage(attacker, cell);
      preDamage(attacker, cell, damage);
      damage(attacker, cell, damage);
      postDamage(attacker, cell, damage);
    }
  }

  @Override
  public ImmutableList<Target> createTargets(Movements movements, Cell from) {
    throw new UnsupportedOperationException("override me!");
  }

  /**
   * Default implementation damage the character in the target cell if there any.
   * Override me to change damage application.
   */
  void damage(Character attacker, Cell target, int damage) {
    for (Character defender : target.character().asSet()) {
      defender.damageBy(damage);
    }
  }

  /**
   * Default implementation does nothing.
   * Override me to add pre-damage effects.
   */
  void preDamage(Character attacker, Cell target, int damage) {
  }

  /**
   * Default implementation does nothing.
   * Override me to add post-damage effects.
   */
  void postDamage(Character attacker, Cell target, int damage) {
    if (lifeSteal) {
      attacker.healBy((int) (lifeStealRate * damage));
    }
    if (recoil) {
      attacker.damageBy((int) (recoilRate * damage));
    }
  }

  /**
   * Get the damage attack to inflict upon target cell for this weapon.
   * Default implementation reduces attack power by the character's defense if there are any.
   * Override me to change damage calculation.
   */
  int getDamage(Character attacker, Cell target) {
    if (target.character().isPresent()) {
      return Math.max(attackPower - target.character().get().getDefense(), 0);
    }
    return attackPower;
  }
}
