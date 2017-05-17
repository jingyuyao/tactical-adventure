package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;

public class PassiveShip extends BasicShip {

  PassiveShip() {
  }

  @Override
  public AutoPilot getAutoPilot(Movements movements, Cell starting) {
    Movement movement = movements.distanceFrom(starting);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : getWeapons()) {
        for (final Target target : weapon.createTargets(movements, moveCell)) {
          if (canTarget(target)) {
            return new AutoPilot(movement.pathTo(moveCell), new Battle(moveCell, weapon, target));
          }
        }
      }
    }
    return new AutoPilot(null, null);
  }

  private boolean canTarget(Target target) {
    boolean containsPlayer = false;
    boolean containsEnemy = false;

    for (Cell cell : target.getTargetCells()) {
      for (Ship ship : cell.ship().asSet()) {
        switch (ship.getAllegiance()) {
          case PLAYER:
            containsPlayer = true;
            break;
          case ENEMY:
            containsEnemy = true;
            break;
        }
      }
    }

    // Don't hit friendly ships?
    return !containsEnemy && containsPlayer;
  }
}
