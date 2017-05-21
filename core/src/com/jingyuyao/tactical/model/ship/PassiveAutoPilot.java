package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.World;

public class PassiveAutoPilot implements AutoPilot {

  @Override
  public PilotResponse getResponse(World world, Cell shipCell) {
    Preconditions.checkArgument(shipCell.ship().isPresent());
    Ship ship = shipCell.ship().get();
    Movement movement = world.getShipMovement(shipCell);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : ship.getWeapons()) {
        for (final Target target : weapon.createTargets(world, moveCell)) {
          if (canTarget(target)) {
            return new PilotResponse(movement.pathTo(moveCell),
                new Battle(moveCell, weapon, target));
          }
        }
      }
    }
    return new PilotResponse(null, null);
  }

  private boolean canTarget(Target target) {
    boolean containsPlayer = false;
    boolean containsEnemy = false;

    for (Cell cell : target.getTargetCells()) {
      for (Ship ship : cell.ship().asSet()) {
        switch (ship.getGroup()) {
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
