package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

public class Cell {

  private final Coordinate coordinate;
  private final Terrain terrain;
  private Character character;

  public Cell(Coordinate coordinate, Terrain terrain) {
    this.coordinate = coordinate;
    this.terrain = terrain;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Terrain getTerrain() {
    return terrain;
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

  public boolean hasPlayer() {
    return hasCharacter() && character instanceof Player;
  }

  public Player getPlayer() {
    return (Player) character;
  }
}
