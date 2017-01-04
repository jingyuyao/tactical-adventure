package com.jingyuyao.tactical.model;

import com.google.common.base.MoreObjects;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;

public class AttackPlan {
  private final Player attackingPlayer;
  private final Enemy targetEnemy;
  /** Never null. */
  private final Weapon playerWeapon;
  /** Can be null. */
  private final Weapon enemyWeapon;

  // TODO: add terrains
  AttackPlan(Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon, Weapon enemyWeapon) {
    this.attackingPlayer = attackingPlayer;
    this.targetEnemy = targetEnemy;
    this.playerWeapon = playerWeapon;
    this.enemyWeapon = enemyWeapon;
  }

  public Player getAttackingPlayer() {
    return attackingPlayer;
  }

  public Enemy getTargetEnemy() {
    return targetEnemy;
  }

  /** Executes this attack plan, make HP reduction calculations */
  public void execute() {
    // TODO: complete me
    targetEnemy.damageBy(playerWeapon.getAttackPower());
    playerWeapon.useOnce();

    if (targetEnemy.isAlive() && enemyWeapon != null) {
      attackingPlayer.damageBy(enemyWeapon.getAttackPower());
      enemyWeapon.useOnce();
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("attackingPlayer", attackingPlayer.toString())
        .add("targetEnemy", targetEnemy.toString())
        .toString();
  }
}
