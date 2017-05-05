package com.jingyuyao.tactical.model.battle;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;

public class Battle2 {

  private final Cell attackerCell;
  private final Weapon weapon;
  private final Target target;
  private final MyFuture future;

  public Battle2(Cell attackerCell, Weapon weapon, Target target, MyFuture future) {
    this.attackerCell = attackerCell;
    this.weapon = weapon;
    this.target = target;
    this.future = future;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Target getTarget() {
    return target;
  }

  public MyFuture getFuture() {
    return future;
  }

  /**
   * Executes the logic of this battle and completes the future.
   */
  public void execute() {
    for (Character attacker : attackerCell.character().asSet()) {
      weapon.apply(attacker, target);
      attacker.useWeapon(weapon);
      for (Cell cell : target.getTargetCells()) {
        for (Character character : cell.character().asSet()) {
          character.useEquippedArmors();
          if (character.getHp() == 0) {
            cell.removeCharacter();
          }
        }
      }
      if (attacker.getHp() == 0) {
        attackerCell.removeCharacter();
      }
    }
    future.done();
  }
}
