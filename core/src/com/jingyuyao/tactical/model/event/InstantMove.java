package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.object.Character;

public class InstantMove implements ModelEvent {
    private final Character character;
    private final Coordinate destination;

    public InstantMove(Character character, Coordinate destination) {
        this.character = character;
        this.destination = destination;
    }

    public Character getCharacter() {
        return character;
    }

    public Coordinate getDestination() {
        return destination;
    }
}
