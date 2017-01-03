package com.jingyuyao.tactical.model.character.event;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class Move extends ObjectEvent<Character> {
    private final ImmutableList<Coordinate> path;

    public Move(Character character, ImmutableList<Coordinate> path) {
        super(character);
        this.path = path;
    }

    public ImmutableList<Coordinate> getPath() {
        return path;
    }
}
