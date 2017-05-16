package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;

public class PassiveEnemy extends BaseShip implements Enemy {

  PassiveEnemy() {
  }

  @Override
  public Retaliation getRetaliation(Movements movements, Cell starting) {
    Movement movement = movements.distanceFrom(starting);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : getWeapons()) {
        for (final Target target : weapon.createTargets(movements, moveCell)) {
          if (canTarget(target)) {
            return new Retaliation(movement.pathTo(moveCell), new Battle(moveCell, weapon, target));
          }
        }
      }
    }
    return new Retaliation(null, null);
  }

  private boolean canTarget(Target target) {
    boolean containsPlayer = false;
    boolean containsEnemy = false;

    for (Cell cell : target.getTargetCells()) {
      if (cell.player().isPresent()) {
        containsPlayer = true;
      }

      if (cell.enemy().isPresent()) {
        containsEnemy = true;
      }
    }

    // Don't hit friendly ships?
    return !containsEnemy && containsPlayer;
  }
}
