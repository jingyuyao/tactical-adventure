package com.jingyuyao.tactical.data;

import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LevelProgress {

  private List<Player> inactivePlayers = new ArrayList<>();
  private Map<Coordinate, Player> activePlayers = new HashMap<>();
  private Map<Coordinate, Enemy> activeEnemies = new HashMap<>();

  public LevelProgress() {

  }

  // TODO: should be able to choose which player goes to which spawn
  public LevelProgress(GameSave gameSave, LevelData levelData) {
    List<Player> players = gameSave.getPlayers();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      if (i < playerSpawns.size()) {
        activePlayers.put(playerSpawns.get(i), player);
      } else {
        inactivePlayers.add(player);
      }
    }

    for (Entry<Coordinate, Enemy> entry : levelData.getEnemies().entrySet()) {
      activeEnemies.put(entry.getKey(), entry.getValue());
    }
  }

  public List<Player> getInactivePlayers() {
    return inactivePlayers;
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

  public void update(Iterable<Cell> cells) {
    activePlayers.clear();
    activeEnemies.clear();
    for (Cell cell : cells) {
      Coordinate coordinate = cell.getCoordinate();
      if (cell.hasPlayer()) {
        activePlayers.put(coordinate, cell.getPlayer());
      } else if (cell.hasEnemy()) {
        activeEnemies.put(coordinate, cell.getEnemy());
      }
    }
  }
}
