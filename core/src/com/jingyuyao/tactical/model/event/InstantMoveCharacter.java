package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;

public class InstantMoveCharacter {

  private final Character character;
  private final Cell destination;

  public InstantMoveCharacter(Character character, Cell destination) {
    this.character = character;
    this.destination = destination;
  }

  public Character getCharacter() {
    return character;
  }

  public Cell getDestination() {
    return destination;
  }
}
