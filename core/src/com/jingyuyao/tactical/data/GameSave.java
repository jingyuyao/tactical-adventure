package com.jingyuyao.tactical.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game save containing meta data and the player's ships.
 */
public class GameSave {

  private int currentLevel = 1;
  private List<Ship> playerShips = new ArrayList<>();

  private GameSave() {
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

  ImmutableList<Ship> getPlayerShips() {
    return ImmutableList.copyOf(playerShips);
  }

  /**
   * Replace the all the player ships with the ones from the world, both active and inactive.
   */
  void replacePlayerShipsFrom(World world) {
    playerShips.clear();
    for (Ship ship : Iterables.concat(world.getInactiveShips(), world.getShipSnapshot().values())) {
      if (ship.inGroup(ShipGroup.PLAYER)) {
        playerShips.add(ship);
      }
    }
  }
}
