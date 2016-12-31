package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;

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
