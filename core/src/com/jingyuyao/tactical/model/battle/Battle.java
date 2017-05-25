package com.jingyuyao.tactical.model.battle;

import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.ArrayList;
import java.util.List;

public class Battle {

  private final Cell attackerCell;
  private final Weapon weapon;
  private final Target target;
  private final List<Cell> deadCells;

  public Battle(Cell attackerCell, Weapon weapon, Target target) {
    this.attackerCell = attackerCell;
    this.weapon = weapon;
    this.target = target;
    this.deadCells = new ArrayList<>();
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Target getTarget() {
    return target;
  }

  /**
   * Return the cells that has dead ships after this battle.
   */
  public List<Cell> getDeadCells() {
    return deadCells;
  }

  /**
   * Executes the logic of this battle.
   */
  public void execute() {
    for (Ship attacker : attackerCell.ship().asSet()) {
      weapon.apply(attacker, target);
      attacker.useWeapon(weapon);
      for (Cell cell : target.getTargetCells()) {
        for (Ship ship : cell.ship().asSet()) {
          ship.useEquippedArmors();
        }
        addDeadShip(cell);
      }
    }
    addDeadShip(attackerCell);
  }

  private void addDeadShip(Cell cell) {
    for (Ship ship : cell.ship().asSet()) {
      if (ship.getHp() == 0) {
        deadCells.add(cell);
      }
    }
  }
}
