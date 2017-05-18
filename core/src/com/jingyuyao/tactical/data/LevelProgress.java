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

/**
 * Contains the current progress of a level. Basically a merger of {@link GameData} and {@link
 * LevelData}.
 */
public class LevelProgress {

  private List<Ship> reservedPlayerShips = new ArrayList<>();
  private Map<Coordinate, Ship> playerShips = new HashMap<>();
  private Map<Coordinate, Ship> enemyShips = new HashMap<>();
  private Turn turn = new Turn();

  LevelProgress() {

  }

  // TODO: should be able to choose which player goes to which spawn
  LevelProgress(GameData gameData, LevelData levelData) {
    List<Ship> playerShips = gameData.getPlayerShips();
    List<Coordinate> playerSpawns = levelData.getPlayerSpawns();
    for (int i = 0; i < playerShips.size(); i++) {
      Ship player = playerShips.get(i);
      if (i < playerSpawns.size()) {
        this.playerShips.put(playerSpawns.get(i), player);
      } else {
        reservedPlayerShips.add(player);
      }
    }
    enemyShips.putAll(levelData.getShips());
  }

  public List<Ship> getReservedPlayerShips() {
    return reservedPlayerShips;
  }

  public Map<Coordinate, Ship> getPlayerShips() {
    return playerShips;
  }

  public Map<Coordinate, Ship> getEnemyShips() {
    return enemyShips;
  }

  public Map<Coordinate, Ship> getShips() {
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    shipMap.putAll(playerShips);
    shipMap.putAll(enemyShips);
    return shipMap;
  }

  public Turn getTurn() {
    return turn;
  }

  void update(World world, WorldState worldState) {
    playerShips.clear();
    enemyShips.clear();
    for (Cell cell : world.getShipSnapshot()) {
      Coordinate coordinate = cell.getCoordinate();
      for (Ship ship : cell.ship().asSet()) {
        switch (ship.getAllegiance()) {
          case PLAYER:
            playerShips.put(coordinate, ship);
            break;
          case ENEMY:
            enemyShips.put(coordinate, ship);
            break;
        }
      }
    }
    turn = worldState.getTurn();
  }
}
