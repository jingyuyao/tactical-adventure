package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class InstantMove extends AbstractEvent<Character> {

  private final Coordinate destination;

  public InstantMove(Character character, Coordinate destination) {
    super(character);
    this.destination = destination;
  }

  public Coordinate getDestination() {
    return destination;
  }
}
