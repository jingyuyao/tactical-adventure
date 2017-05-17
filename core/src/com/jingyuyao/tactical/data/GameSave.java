package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.ship.Ship;
import java.util.ArrayList;
import java.util.List;

public class GameSave {

  private int currentLevel = 1;
  private List<Ship> players = new ArrayList<>();

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  List<Ship> getPlayers() {
    return players;
  }

  void update(LevelProgress levelProgress) {
    players.clear();
    players.addAll(levelProgress.getActivePlayers().values());
    players.addAll(levelProgress.getInactivePlayers());
  }
}
