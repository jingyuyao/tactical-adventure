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
        bind(Characters.class);
        bind(Terrains.class);
        bind(MapFactory.class);
        bind(TargetsFactory.class);
    }

    @Provides
    @MapObject.InitialMarkers
    List<Marker> provideInitialMarkers() {
        return new ArrayList<Marker>();
    }

    @Provides
    @Singleton
    @Characters.BackingCharacterSet
    Set<Character> provideBackingCharacterSet() {
        return new HashSet<Character>();
    }

    @Provides
    @Singleton
    @Terrains.BackingTerrainTable
    Table<Integer, Integer, Terrain> provideBackingTerrainTable() {
        return HashBasedTable.create();
    }
}
