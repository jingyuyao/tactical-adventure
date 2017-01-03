package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.State;

public class NewMap implements ModelEvent {
    private final Iterable<Character> characters;
    private final Iterable<Terrain> terrains;
    private final State initialState;

    public NewMap(Iterable<Character> characters, Iterable<Terrain> terrains, State initialState) {
        this.characters = characters;
        this.terrains = terrains;
        this.initialState = initialState;
    }

    public Iterable<Character> getCharacters() {
        return characters;
    }

    public Iterable<Terrain> getTerrains() {
        return terrains;
    }

    public State getInitialState() {
        return initialState;
    }
}
