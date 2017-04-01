package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Player;
import java.util.ArrayList;
import java.util.List;

public class GameSave {

  private int currentLevel = 1;
  private List<Player> players = new ArrayList<>();

  public int getCurrentLevel() {
    return currentLevel;
  }

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  List<Player> getPlayers() {
    return players;
  }
}
