package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.Map;

public class LoadedLevel {

  private final Map<Coordinate, Terrain> terrainMap;
  private final Map<Coordinate, Character> characterMap;
  private final Turn turn;
  private final Script script;

  LoadedLevel(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Character> characterMap,
      Turn turn,
      Script script) {
    this.terrainMap = terrainMap;
    this.characterMap = characterMap;
    this.turn = turn;
    this.script = script;
  }

  public Map<Coordinate, Terrain> getTerrainMap() {
    return terrainMap;
  }

  public Map<Coordinate, Character> getCharacterMap() {
    return characterMap;
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }
}
