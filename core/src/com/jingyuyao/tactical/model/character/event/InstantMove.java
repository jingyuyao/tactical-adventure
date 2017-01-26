package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.Coordinate;

public class InstantMove extends ObjectEvent<Character> {

  private final Coordinate destination;

  public InstantMove(Character character, Coordinate destination) {
    super(character);
    this.destination = destination;
  }

  public Coordinate getDestination() {
    return destination;
  }
}
