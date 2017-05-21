package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main game save containing meta data and the player's ships.
 */
public class GameSave {

  private int currentLevel = 1;
  /**
   * Ships that are not in the world.
   */
  private List<Ship> inactiveShips = new ArrayList<>();
  /**
   * Ships that are currently in the world. Saved to we can rollback
   */
  private List<Ship> activeShips = new ArrayList<>();

  private GameSave() {
  }

  GameSave(int currentLevel, List<Ship> inactiveShips, List<Ship> activeShips) {
    this.currentLevel = currentLevel;
    this.inactiveShips = inactiveShips;
    this.activeShips = activeShips;
  }

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  /**
   * Actives inactive ship to the given spawn coordinates. Activated ships are moved from the
   * inactive list to active list. Return a map of the activated ships.
   */
  Map<Coordinate, Ship> activateShips(List<Coordinate> spawns) {
    Map<Coordinate, Ship> activated = new HashMap<>();
    for (int i = 0; i < spawns.size() && i < inactiveShips.size(); i++) {
      activated.put(spawns.get(i), inactiveShips.get(i));
      activeShips.add(inactiveShips.get(i));
    }
    inactiveShips.removeAll(activated.values());
    return activated;
  }

  /**
   * Replaces the active ships list with the player ships from the given {@link World}.
   */
  void replaceActiveShipsFrom(World world) {
    activeShips.clear();
    for (Ship ship : world.getShipSnapshot().values()) {
      if (ship.getGroup().equals(ShipGroup.PLAYER)) {
        activeShips.add(ship);
      }
    }
  }

  /**
   * Move all currently active ships back into being inactive.
   */
  void deactivateShips() {
    inactiveShips.addAll(activeShips);
    activeShips.clear();
  }
}
