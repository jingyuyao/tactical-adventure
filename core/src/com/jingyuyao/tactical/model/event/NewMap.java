package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.State;
import java.util.List;

public class NewMap implements ModelEvent {

  private final int width;
  private final int height;
  private final List<Player> players;
  private final List<Enemy> enemies;
  private final List<Terrain> terrains;
  private final State initialState;

  public NewMap(
      int width,
      int height,
      List<Player> players,
      List<Enemy> enemies,
      List<Terrain> terrains,
      State initialState) {
    this.width = width;
    this.height = height;
    this.players = players;
    this.enemies = enemies;
    this.terrains = terrains;
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

  public List<Terrain> getTerrains() {
    return terrains;
  }

  public State getInitialState() {
    return initialState;
  }
}
