package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.Cell;

public class InstantMove extends ObjectEvent<Character> {

  private final Cell destination;

  public InstantMove(Character character, Cell destination) {
    super(character);
    this.destination = destination;
  }

  public Cell getDestination() {
    return destination;
  }
}
