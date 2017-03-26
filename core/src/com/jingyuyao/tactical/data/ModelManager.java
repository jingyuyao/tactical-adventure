package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.CellFactory;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.World;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ModelManager {

  private final Model model;
  private final World world;
  private final CellFactory cellFactory;
  private final Provider<Waiting> waitingProvider;

  @Inject
  ModelManager(
      Model model, World world, CellFactory cellFactory, Provider<Waiting> waitingProvider) {
    this.model = model;
    this.world = world;
    this.cellFactory = cellFactory;
    this.waitingProvider = waitingProvider;
  }

  public void load(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Player> playerMap,
      Map<Coordinate, Enemy> enemyMap) {
    Map<Coordinate, Cell> cellMap = new HashMap<>();
    for (Entry<Coordinate, Terrain> entry : terrainMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated terrain detected");
      }
      Cell cell = cellFactory.create(coordinate, entry.getValue());
      cellMap.put(coordinate, cell);
    }
    for (Entry<Coordinate, Player> entry : playerMap.entrySet()) {
      add(entry.getKey(), entry.getValue(), cellMap);
    }
    for (Entry<Coordinate, Enemy> entry : enemyMap.entrySet()) {
      add(entry.getKey(), entry.getValue(), cellMap);
    }
    model.load(cellMap.values(), waitingProvider.get());
  }

  public Map<Coordinate, Player> getActivePlayerMap() {
    Map<Coordinate, Player> playerMap = new HashMap<>();
    for (Cell cell : world.getCells()) {
      if (cell.hasPlayer()) {
        playerMap.put(cell.getCoordinate(), cell.getPlayer());
      }
    }
    return playerMap;
  }

  public Map<Coordinate, Enemy> getActiveEnemyMap() {
    Map<Coordinate, Enemy> enemyMap = new HashMap<>();
    for (Cell cell : world.getCells()) {
      if (cell.hasEnemy()) {
        enemyMap.put(cell.getCoordinate(), cell.getEnemy());
      }
    }
    return enemyMap;
  }

  private void add(Coordinate coordinate, Character character, Map<Coordinate, Cell> cellMap) {
    if (!cellMap.containsKey(coordinate)) {
      throw new IllegalArgumentException("Character not on a terrain");
    }
    Cell cell = cellMap.get(coordinate);
    if (cell.hasCharacter()) {
      throw new IllegalArgumentException("Character occupying same space as another");
    }
    cell.spawnCharacter(character);
  }
}
