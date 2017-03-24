package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.MapModule.CellEventBus;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

public class Cell {

  private final EventBus cellEventBus;
  private final Coordinate coordinate;
  private final Terrain terrain;
  private Character character;

  @Inject
  Cell(
      @CellEventBus EventBus cellEventBus,
      @Assisted Coordinate coordinate,
      @Assisted Terrain terrain) {
    this.cellEventBus = cellEventBus;
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

  public boolean hasEnemy() {
    return hasCharacter() && character instanceof Enemy;
  }

  public Enemy getEnemy() {
    return (Enemy) character;
  }

  public void moveCharacterTo(Cell cell) {
    Preconditions.checkState(hasCharacter());
    Preconditions.checkArgument(!cell.hasCharacter());
    cell.setCharacter(character);
    setCharacter(null);
  }
}
