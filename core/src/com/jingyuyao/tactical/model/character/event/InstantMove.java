package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.ObjectEvent;

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
