package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final Characters characters;
  private final Terrains terrains;
  private final MapState mapState;

  @Inject
  Model(Characters characters, Terrains terrains, MapState mapState) {
    this.characters = characters;
    this.terrains = terrains;
    this.mapState = mapState;
  }

  public void loadMap(
      Iterable<Terrain> terrains,
      Iterable<Player> players,
      Iterable<Enemy> enemies,
      State initialState) {
    // TODO: reset model
    for (Terrain terrain : terrains) {
      this.terrains.add(terrain);
    }
    for (Player player : players) {
      characters.add(player);
    }
    for (Enemy enemy : enemies) {
      characters.add(enemy);
    }
    mapState.initialize(initialState);
  }

  /**
   * Call me before serializing game state
   */
  public void prepForSave() {
    mapState.prepForSave();
  }
}
