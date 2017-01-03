package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.CharacterContainer;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.TerrainGrid;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Markings;
import com.jingyuyao.tactical.model.state.Waiting;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ModelManager {
    private final CharacterContainer characterContainer;
    private final TerrainGrid terrainGrid;
    private final Markings markings;
    private final MapState mapState;
    private final Provider<Waiting> waitingProvider;

    @Inject
    ModelManager(CharacterContainer characterContainer, TerrainGrid terrainGrid, Markings markings, MapState mapState, Provider<Waiting> waitingProvider) {
        this.characterContainer = characterContainer;
        this.terrainGrid = terrainGrid;
        this.markings = markings;
        this.mapState = mapState;
        this.waitingProvider = waitingProvider;
    }

    public void initializeMap(Iterable<Character> characters, Iterable<Terrain> terrains) {
        disposeMap();
        characterContainer.addAll(characters);
        terrainGrid.addAll(terrains);
        mapState.initialize(waitingProvider.get());
    }

    public void disposeMap() {
        markings.dispose();
        mapState.dispose();
        characterContainer.dispose();
        terrainGrid.dispose();
    }
}
