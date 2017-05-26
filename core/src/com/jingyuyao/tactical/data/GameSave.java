package com.jingyuyao.tactical.data;

import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.World;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game save containing meta data and the player's ships.
 */
public class GameSave implements Serializable {

  private int currentLevel = 1;
  private List<Ship> playerShips = new ArrayList<>();

  GameSave() {
  }

  GameSave(int currentLevel, List<Ship> playerShips) {
    this.currentLevel = currentLevel;
    this.playerShips = playerShips;
  }

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  List<Ship> getPlayerShips() {
    return playerShips;
  }

  /**
   * Replace the all the player ships with the ones from the world, both active and inactive.
   */
  void replacePlayerShipsFrom(World world) {
    playerShips.clear();
    for (Ship ship : Iterables.concat(world.getInactiveShips(), world.getActiveShips().values())) {
      if (ship.inGroup(ShipGroup.PLAYER)) {
        playerShips.add(ship);
      }
    }
  }
}
