package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.ship.Ship;
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

  private List<Ship> inactivePlayers = new ArrayList<>();
  private Map<Coordinate, Ship> activePlayers = new HashMap<>();
  private Map<Coordinate, Ship> activeEnemies = new HashMap<>();
  private Turn turn = new Turn();

  LevelProgress() {

  }

  // TODO: should be able to choose which player goes to which spawn
  LevelProgress(GameSave gameSave, LevelData levelData) {
    List<Ship> players = gameSave.getPlayers();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < players.size(); i++) {
      Ship player = players.get(i);
      if (i < playerSpawns.size()) {
        activePlayers.put(playerSpawns.get(i), player);
      } else {
        inactivePlayers.add(player);
      }
    }
    activeEnemies.putAll(levelData.getEnemies());
  }

  public Map<Coordinate, Ship> getActivePlayers() {
    return activePlayers;
  }

  public Map<Coordinate, Ship> getActiveEnemies() {
    return activeEnemies;
  }

  public List<Ship> getInactivePlayers() {
    return inactivePlayers;
  }

  public Map<Coordinate, Ship> getActiveShips() {
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    shipMap.putAll(activePlayers);
    shipMap.putAll(activeEnemies);
    return shipMap;
  }

  public Turn getTurn() {
    return turn;
  }

  void update(World world, WorldState worldState) {
    activePlayers.clear();
    activeEnemies.clear();
    for (Cell cell : world.getShipSnapshot()) {
      Coordinate coordinate = cell.getCoordinate();
      for (Ship ship : cell.ship().asSet()) {
        switch (ship.getAllegiance()) {
          case PLAYER:
            activePlayers.put(coordinate, ship);
            break;
          case ENEMY:
            activeEnemies.put(coordinate, ship);
            break;
        }
      }
    }
    turn = worldState.getTurn();
  }
}
