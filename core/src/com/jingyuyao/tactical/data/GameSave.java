package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game save
 */
public class GameSave {

  private int currentLevel = 1;
  private List<Ship> inactiveShips = new ArrayList<>();
  private List<Ship> activeShips = new ArrayList<>();

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  List<Ship> getInactiveShips() {
    return inactiveShips;
  }

  List<Ship> getActiveShips() {
    return activeShips;
  }

  void update(LevelProgress levelProgress) {
    inactiveShips.clear();
    inactiveShips.addAll(levelProgress.getReservedPlayerShips());
    for (Ship ship : levelProgress.getShips().values()) {
      if (ship.getAllegiance().equals(Allegiance.PLAYER)) {
        inactiveShips.add(ship);
      }
    }
  }
}
