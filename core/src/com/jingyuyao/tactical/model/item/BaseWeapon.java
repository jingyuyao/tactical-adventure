package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;

/**
 * A basic {@link Weapon} that does constant damage to all the {@link Target}.
 */
class BaseWeapon extends BaseItem implements Weapon {

  private int attackPower;
  private float lifeStealRate;
  private float recoilRate;

  BaseWeapon() {
  }

  BaseWeapon(int attackPower, float lifeStealRate, float recoilRate) {
    this.attackPower = attackPower;
    this.lifeStealRate = lifeStealRate;
    this.recoilRate = recoilRate;
  }

  @Override
  public ResourceKey getDescription() {
    if (lifeStealRate > 0) {
      return ModelBundle.ITEM_DESCRIPTION
          .get("lifeStealWeapon", attackPower, lifeStealRate * 100);
    }
    if (recoilRate > 0) {
      return ModelBundle.ITEM_DESCRIPTION
          .get("recoilWeapon", attackPower, recoilRate * 100);
    }
    return ModelBundle.ITEM_DESCRIPTION.get("normalWeapon", attackPower);
  }

  /**
   * Apply pre-damage, damage, and post-damage effects to each targeted cell.
   */
  @Override
  public void apply(Ship attacker, Target target) {
    for (Cell cell : target.getTargetCells()) {
      int damage = getDamage(attacker, cell);
      preDamage(attacker, cell, damage);
      damage(attacker, cell, damage);
      postDamage(attacker, cell, damage);
    }
  }

  @Override
  public ImmutableList<Target> createTargets(World world, Cell from) {
    throw new UnsupportedOperationException("override me!");
  }

  /**
   * Default implementation damage the ship in the target cell if there any.
   * Override me to change damage application.
   */
  void damage(Ship attacker, Cell target, int damage) {
    for (Ship defender : target.ship().asSet()) {
      defender.damageBy(damage);
    }
  }

  /**
   * Default implementation does nothing.
   * Override me to add pre-damage effects.
   */
  void preDamage(Ship attacker, Cell target, int damage) {
  }

  /**
   * Default implementation does nothing.
   * Override me to add post-damage effects.
   */
  void postDamage(Ship attacker, Cell target, int damage) {
    // can't life steal if there is nobody to steal it from
    if (target.ship().isPresent()) {
      attacker.healBy((int) (lifeStealRate * damage));
    }
    attacker.damageBy((int) (recoilRate * damage));
  }

  /**
   * Get the damage attack to inflict upon target cell for this weapon.
   * Default implementation reduces attack power by the ship's defense if there are any.
   * Override me to change damage calculation.
   */
  int getDamage(Ship attacker, Cell target) {
    if (target.ship().isPresent()) {
      return Math.max(attackPower - target.ship().get().getDefense(), 0);
    }
    return 0;
  }
}