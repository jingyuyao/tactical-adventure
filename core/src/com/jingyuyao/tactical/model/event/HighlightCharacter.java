package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

public class HighlightCharacter implements ModelEvent {
    private final Character character;
    private final Terrain terrain;

    public HighlightCharacter(Character character, Terrain terrain) {
        this.character = character;
        this.terrain = terrain;
    }

    public Character getCharacter() {
        return character;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}
