package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSave {

  private int currentLevel = 1;
  private boolean inProgress = false;
  private List<Player> startingPlayers = new ArrayList<>();
  // active + inactive players makes up the next list of starting players
  private List<Player> inactivePlayers = new ArrayList<>();
  private Map<Coordinate, Player> activePlayers = new HashMap<>();
  private Map<Coordinate, Enemy> activeEnemies = new HashMap<>();

  public int getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  public boolean isInProgress() {
    return inProgress;
  }

  public void setInProgress(boolean inProgress) {
    this.inProgress = inProgress;
  }

  public List<Player> getStartingPlayers() {
    return startingPlayers;
  }

  public List<Player> getInactivePlayers() {
    return inactivePlayers;
  }

  public Map<Coordinate, Player> getActivePlayers() {
    return activePlayers;
  }

  public Map<Coordinate, Enemy> getActiveEnemies() {
    return activeEnemies;
  }

  public void clearInProgressData() {
    inProgress = false;
    inactivePlayers.clear();
    activePlayers.clear();
    activeEnemies.clear();
  }
}
