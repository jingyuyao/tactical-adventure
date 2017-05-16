package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.ArrayList;
import java.util.List;

public class Battle {

  private final Cell attackerCell;
  private final Weapon weapon;
  private final Target target;
  private final List<Ship> death;

  public Battle(Cell attackerCell, Weapon weapon, Target target) {
    this.attackerCell = attackerCell;
    this.weapon = weapon;
    this.target = target;
    this.death = new ArrayList<>();
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Target getTarget() {
    return target;
  }

  public ImmutableList<Ship> getDeath() {
    return ImmutableList.copyOf(death);
  }

  /**
   * Executes the logic of this battle.
   */
  public void execute() {
    for (Ship attacker : attackerCell.character().asSet()) {
      weapon.apply(attacker, target);
      attacker.useWeapon(weapon);
      for (Cell cell : target.getTargetCells()) {
        for (Ship ship : cell.character().asSet()) {
          ship.useEquippedArmors();
        }
        checkDeath(cell);
      }
    }
    checkDeath(attackerCell);
  }

  private void checkDeath(Cell cell) {
    for (Ship ship : cell.character().asSet()) {
      if (ship.getHp() == 0) {
        cell.removeCharacter();
        death.add(ship);
      }
    }
  }
}
