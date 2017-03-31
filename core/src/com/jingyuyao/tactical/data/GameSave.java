package com.jingyuyao.tactical.data;

import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

  void setCurrentLevel(int currentLevel) {
    this.currentLevel = currentLevel;
  }

  public boolean isInProgress() {
    return inProgress;
  }

  public void setInProgress(boolean inProgress) {
    this.inProgress = inProgress;
  }

  List<Player> getStartingPlayers() {
    return startingPlayers;
  }

  List<Player> getInactivePlayers() {
    return inactivePlayers;
  }

  void addInactivePlayer(Player player) {
    inactivePlayers.add(player);
  }

  Map<Coordinate, Player> getActivePlayers() {
    return activePlayers;
  }

  public void addActivePlayer(Coordinate coordinate, Player player) {
    activePlayers.put(coordinate, player);
  }

  Map<Coordinate, Enemy> getActiveEnemies() {
    return activeEnemies;
  }

  public void addActiveEnemy(Coordinate coordinate, Enemy enemy) {
    activeEnemies.put(coordinate, enemy);
  }

  public Map<Coordinate, Character> getActiveCharacters() {
    Map<Coordinate, Character> characterMap = new HashMap<>();
    Iterable<Entry<Coordinate, ? extends Character>> entries =
        Iterables.concat(activePlayers.entrySet(), activeEnemies.entrySet());

    for (Entry<Coordinate, ? extends Character> entry : entries) {
      Coordinate coordinate = entry.getKey();
      if (characterMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Some player and enemy occupy the same coordinate");
      }
      Character character = entry.getValue();
      characterMap.put(coordinate, character);
    }

    return characterMap;
  }

  public void clearActiveCharacters() {
    activePlayers.clear();
    activeEnemies.clear();
  }

  public void clearLevelProgress() {
    inProgress = false;
    inactivePlayers.clear();
    clearActiveCharacters();
  }
}
