package com.jingyuyao.tactical.model.event;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.object.Character;

public class Move implements ModelEvent {
    private final Character character;
    private final ImmutableList<Coordinate> path;

    public Move(Character character, ImmutableList<Coordinate> path) {
        this.character = character;
        this.path = path;
    }

    public Character getCharacter() {
        return character;
    }

    public ImmutableList<Coordinate> getPath() {
        return path;
    }
}
