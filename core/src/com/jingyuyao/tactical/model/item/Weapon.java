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

  private int attackPower;
  private float lifeStealRate;
  private float recoilRate;

  Weapon() {
  }

  Weapon(String name, int usageLeft, int attackPower, float lifeStealRate, float recoilRate) {
    super(name, usageLeft);
    this.attackPower = attackPower;
    this.lifeStealRate = lifeStealRate;
    this.recoilRate = recoilRate;
  }

  @Override
  public StringKey getDescription() {
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
      return Math.max(attackPower - target.ship().get().getDefense(), 0);
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
    if (lifeStealRate > 0 && cellDamage > 0) {
      attacker.healBy((int) (lifeStealRate * cellDamage));
    }
  }

  private void postTargetDamageEffects(Ship attacker) {
    if (recoilRate > 0) {
      attacker.damageBy((int) (recoilRate * attackPower));
    }
  }
}
