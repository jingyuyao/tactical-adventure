package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Grid;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.State;
import java.util.List;

public class NewMap implements ModelEvent {

  private final int width;
  private final int height;
  private final List<Player> players;
  private final List<Enemy> enemies;
  private final Grid<Terrain> terrainGrid;
  private final State initialState;

  public NewMap(
      int width,
      int height,
      List<Player> players,
      List<Enemy> enemies,
      Grid<Terrain> terrainGrid,
      State initialState) {
    this.width = width;
    this.height = height;
    this.players = players;
    this.enemies = enemies;
    this.terrainGrid = terrainGrid;
    this.initialState = initialState;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<Enemy> getEnemies() {
    return enemies;
  }

  public Grid<Terrain> getTerrainGrid() {
    return terrainGrid;
  }

  public State getInitialState() {
    return initialState;
  }
}
