package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.Map;

public class LoadedLevel {

  private final Map<Coordinate, Terrain> terrainMap;
  private final Map<Coordinate, Character> characterMap;
  private final int turn;

  LoadedLevel(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Character> characterMap,
      int turn) {
    this.terrainMap = terrainMap;
    this.characterMap = characterMap;
    this.turn = turn;
  }

  public Map<Coordinate, Terrain> getTerrainMap() {
    return terrainMap;
  }

  public Map<Coordinate, Character> getCharacterMap() {
    return characterMap;
  }

  public int getTurn() {
    return turn;
  }
}
