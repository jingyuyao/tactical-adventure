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
import java.util.Map.Entry;

/**
 * Contains the current progress of a level. Basically a merger of {@link GameData} and {@link
 * LevelData}.
 */
public class LevelProgress {

  private List<Ship> reservedPlayerShips = new ArrayList<>();
  private Map<Coordinate, Ship> ships = new HashMap<>();
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
        ships.put(playerSpawns.get(i), player);
      } else {
        reservedPlayerShips.add(player);
      }
    }
    ships.putAll(levelData.getShips());
  }

  public List<Ship> getReservedPlayerShips() {
    return reservedPlayerShips;
  }

  public Map<Coordinate, Ship> getShips() {
    return ships;
  }

  public Turn getTurn() {
    return turn;
  }

  void update(World world, WorldState worldState) {
    ships.clear();
    for (Entry<Cell, Ship> entry : world.getShipSnapshot().entrySet()) {
      ships.put(entry.getKey().getCoordinate(), entry.getValue());
    }
    turn = worldState.getTurn();
  }
}
