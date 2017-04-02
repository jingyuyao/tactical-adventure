package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelProgress {

  private List<Player> inactivePlayers = new ArrayList<>();
  private Map<Coordinate, Player> activePlayers = new HashMap<>();
  private Map<Coordinate, Enemy> activeEnemies = new HashMap<>();

  LevelProgress() {

  }

  // TODO: should be able to choose which player goes to which spawn
  LevelProgress(GameSave gameSave, LevelData levelData) {
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
    activeEnemies.putAll(levelData.getEnemies());
  }

  public Map<Coordinate, Player> getActivePlayers() {
    return activePlayers;
  }

  public Map<Coordinate, Enemy> getActiveEnemies() {
    return activeEnemies;
  }

  List<Player> getInactivePlayers() {
    return inactivePlayers;
  }

  Map<Coordinate, Character> getActiveCharacters() {
    Map<Coordinate, Character> characterMap = new HashMap<>();
    characterMap.putAll(activePlayers);
    characterMap.putAll(activeEnemies);
    return characterMap;
  }

  void update(World world) {
    activePlayers.clear();
    activeEnemies.clear();
    for (Cell cell : world.getCharacterSnapshot()) {
      Coordinate coordinate = cell.getCoordinate();
      if (cell.hasPlayer()) {
        activePlayers.put(coordinate, cell.getPlayer());
      } else if (cell.hasEnemy()) {
        activeEnemies.put(coordinate, cell.getEnemy());
      }
    }
  }
}
