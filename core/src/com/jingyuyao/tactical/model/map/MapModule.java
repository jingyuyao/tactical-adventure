package com.jingyuyao.tactical.model.map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.mark.Marker;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CharacterContainer.class);
        bind(TerrainGrid.class);
        bind(MapFactory.class);
    }

    @Provides
    @MapObject.InitialMarkers
    List<Marker> provideMarkers() {
        return new ArrayList<Marker>();
    }

    @Provides
    @Singleton
    @CharacterContainer.InitialCharacterSet
    Set<Character> provideInitialCharacterSet() {
        return new HashSet<Character>();
    }

    @Provides
    @Singleton
    @com.jingyuyao.tactical.model.map.TerrainGrid.BackingTable
    Table<Integer, Integer, Terrain> providesBackingTable() {
        return HashBasedTable.create();
    }
}
