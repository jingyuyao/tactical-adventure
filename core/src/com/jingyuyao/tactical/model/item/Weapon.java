package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
import java.util.List;

/**
 * An {@link Item} that can affect a {@link Ship}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Item {

  /**
   * Base damage of the weapon.
   */
  private int attackPower;
  /**
   * How much the weapon heals the user based on damage dealt.
   */
  private float leechRate;
  /**
   * How much the weapon hurts the user based on attack power.
   */
  private float recoilRate;
  /**
   * How much of the target ship's armor to ignore.
   */
  private float piercingRate;

  Weapon() {
  }

  Weapon(
      String name, int usageLeft, int attackPower, float leechRate, float recoilRate,
      float piercingRate) {
    super(name, usageLeft);
    this.attackPower = attackPower;
    this.leechRate = leechRate;
    this.recoilRate = recoilRate;
    this.piercingRate = piercingRate;
  }

  @Override
  public StringKey getDescription() {
    return ModelBundle.ITEM_DESCRIPTION
        .get("weapon", attackPower, leechRate, recoilRate, piercingRate);
  }

  /**
   * Apply this weapon's effects to the attacker and the target.
   */
  public void apply(Ship attacker, Target target) {
    for (Cell cell : target.getTargetCells()) {
      int cellDamage = getDamage(cell);
      damageCell(cell, cellDamage);
      postCellDamageEffects(attacker, cellDamage);
    }
    postTargetDamageEffects(attacker);
  }

  /**
   * Override me.
   */
  public List<Target> createTargets(World world, Cell from) {
    return Collections.emptyList();
  }

  private int getDamage(Cell target) {
    if (target.ship().isPresent()) {
      int targetDefense = target.ship().get().getDefense();
      int piercedDefense = (int) (targetDefense * (1.0f - piercingRate));
      return Math.max(attackPower - piercedDefense, 0);
    }
    return 0;
  }

  private void damageCell(Cell target, int cellDamage) {
    for (Ship defender : target.ship().asSet()) {
      if (cellDamage > 0) {
        defender.damageBy(cellDamage);
      }
    }
  }

  private void postCellDamageEffects(Ship attacker, int cellDamage) {
    if (leechRate > 0 && cellDamage > 0) {
      attacker.healBy((int) (leechRate * cellDamage));
    }
  }

  private void postTargetDamageEffects(Ship attacker) {
    if (recoilRate > 0) {
      attacker.damageBy((int) (recoilRate * attackPower));
    }
  }
}
