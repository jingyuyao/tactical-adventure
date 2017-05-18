package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game data;
 */
public class GameData {

  private int currentLevel = 1;
  private List<Ship> playerShips = new ArrayList<>();

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  List<Ship> getPlayerShips() {
    return playerShips;
  }

  void update(LevelProgress levelProgress) {
    playerShips.clear();
    playerShips.addAll(levelProgress.getReservedPlayerShips());
    for (Ship ship : levelProgress.getShips().values()) {
      if (ship.getAllegiance().equals(Allegiance.PLAYER)) {
        playerShips.add(ship);
      }
    }
  }
}
