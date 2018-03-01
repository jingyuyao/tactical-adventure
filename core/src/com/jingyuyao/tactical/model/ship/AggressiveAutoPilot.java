package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.List;

/**
 * An auto pilot that tries to attacks the closest player ship. The search space is constrained by
 * a searchMultiplier which is applied to the move distance of the enemy ship.
 */
// TODO: test me
public class AggressiveAutoPilot implements AutoPilot {

  private float searchMultiplier = 2.0f;

  @Override
  public PilotResponse getResponse(World world, Cell shipCell) {
    Preconditions.checkArgument(shipCell.ship().isPresent());
    Ship ship = shipCell.ship().get();
    Movement movement = world.getShipMovement(shipCell);
    for (Path pathToClosestPlayer : getPathToClosestPlayer(world, shipCell).asSet()) {
      Cell furthestCanMoveTo = null;
      // Go to the closest cell to attach a player
      for (Cell moveCell : pathToClosestPlayer.getTrack()) {
        if (movement.canMoveTo(moveCell)) {
          for (Weapon weapon : ship.getWeapons()) {
            for (Target target : weapon.createTargets(world, moveCell)) {
              if (canTarget(target)) {
                return new PilotResponse(
                    pathToClosestPlayer.truncate(moveCell), new Battle(moveCell, weapon, target));
              }
            }
          }
          furthestCanMoveTo = moveCell;
        }
      }
      // Or travel to the furthest cell it can travel on the path
      if (furthestCanMoveTo != null) {
        return new PilotResponse(pathToClosestPlayer.truncate(furthestCanMoveTo), null);
      }
    }
    return new PilotResponse(null, null);
  }

  private Optional<Path> getPathToClosestPlayer(World world, Cell cell) {
    Movement movement = world.getShipMovement(cell, searchMultiplier);
    List<Cell> nextToPlayerCells = new ArrayList<>();
    for (Cell move : movement.getCells()) {
      if (nextToPlayer(world, move)) {
        nextToPlayerCells.add(move);
      }
    }
    List<Path> potentialPaths = new ArrayList<>();
    for (Cell nextToPlayer : nextToPlayerCells) {
      potentialPaths.add(movement.pathTo(nextToPlayer));
    }
    if (potentialPaths.size() > 0) {
      Path minPath = potentialPaths.get(0);
      for (Path path : potentialPaths) {
        if (path.getTrack().size() < minPath.getTrack().size()) {
          minPath = path;
        }
      }
      return Optional.of(minPath);
    }
    return Optional.absent();
  }

  private boolean nextToPlayer(World world, Cell cell) {
    for (Cell neighbor : world.getNeighbors(cell)) {
      for (Ship ship : neighbor.ship().asSet()) {
        if (ship.inGroup(ShipGroup.PLAYER)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canTarget(Target target) {
    boolean containsPlayer = false;
    boolean containsEnemy = false;

    for (Cell cell : target.getTargetCells()) {
      for (Ship ship : cell.ship().asSet()) {
        if (ship.inGroup(ShipGroup.PLAYER)) {
          containsPlayer = true;
        }
        if (ship.inGroup(ShipGroup.ENEMY)) {
          containsEnemy = true;
        }
      }
    }

    // Don't hit friendly ships?
    return !containsEnemy && containsPlayer;
  }
}
