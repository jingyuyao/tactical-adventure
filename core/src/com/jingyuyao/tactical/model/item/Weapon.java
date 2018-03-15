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
      int damage = getDamage(cell);
      damageCell(cell, damage);
      postDamage(attacker, cell, damage);
    }
  }

  /**
   * Override me.
   */
  public List<Target> createTargets(World world, Cell from) {
    return Collections.emptyList();
  }

  private void damageCell(Cell target, int damage) {
    for (Ship defender : target.ship().asSet()) {
      defender.damageBy(damage);
    }
  }

  private void postDamage(Ship attacker, Cell target, int damage) {
    // can't life steal if there is nobody to steal it from
    if (target.ship().isPresent()) {
      attacker.healBy((int) (lifeStealRate * damage));
    }
    attacker.damageBy((int) (recoilRate * damage));
  }

  private int getDamage(Cell target) {
    if (target.ship().isPresent()) {
      return Math.max(attackPower - target.ship().get().getDefense(), 0);
    }
    return 0;
  }
}
