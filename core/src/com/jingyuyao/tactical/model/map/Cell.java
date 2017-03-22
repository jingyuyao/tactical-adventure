package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;

public class Cell {

  private Terrain terrain;
  private Character character;

  public boolean hasTerrain() {
    return terrain != null;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public void setTerrain(Terrain terrain) {
    this.terrain = terrain;
  }

  public boolean hasCharacter() {
    return character != null;
  }

  public Character getCharacter() {
    return character;
  }

  public void setCharacter(Character character) {
    this.character = character;
  }
}
