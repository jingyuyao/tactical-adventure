package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.WorldState;
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
  private Turn turn = new Turn();

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

  public List<Player> getInactivePlayers() {
    return inactivePlayers;
  }

  public Map<Coordinate, Ship> getActiveCharacters() {
    Map<Coordinate, Ship> characterMap = new HashMap<>();
    characterMap.putAll(activePlayers);
    characterMap.putAll(activeEnemies);
    return characterMap;
  }

  public Turn getTurn() {
    return turn;
  }

  void update(World world, WorldState worldState) {
    activePlayers.clear();
    activeEnemies.clear();
    for (Cell cell : world.getCharacterSnapshot()) {
      Coordinate coordinate = cell.getCoordinate();
      for (Player player : cell.player().asSet()) {
        activePlayers.put(coordinate, player);
      }

      for (Enemy enemy : cell.enemy().asSet()) {
        activeEnemies.put(coordinate, enemy);
      }
    }
    turn = worldState.getTurn();
  }
}
