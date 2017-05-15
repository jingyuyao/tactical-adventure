package com.jingyuyao.tactical.model.battle;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.ArrayList;
import java.util.List;

public class Battle {

  private final Cell attackerCell;
  private final Weapon weapon;
  private final Target target;
  private final List<Character> death;

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

  public ImmutableList<Character> getDeath() {
    return ImmutableList.copyOf(death);
  }

  /**
   * Executes the logic of this battle.
   */
  public void execute() {
    for (Character attacker : attackerCell.character().asSet()) {
      weapon.apply(attacker, target);
      attacker.useWeapon(weapon);
      for (Cell cell : target.getTargetCells()) {
        for (Character character : cell.character().asSet()) {
          character.useEquippedArmors();
        }
        checkDeath(cell);
      }
    }
    checkDeath(attackerCell);
  }

  private void checkDeath(Cell cell) {
    for (Character character : cell.character().asSet()) {
      if (character.getHp() == 0) {
        cell.removeCharacter();
        death.add(character);
      }
    }
  }
}
