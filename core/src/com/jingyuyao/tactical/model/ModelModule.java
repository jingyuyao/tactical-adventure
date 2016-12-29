package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.object.CharacterContainer;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;

public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Map.class).in(Singleton.class);
        bind(TerrainGrid.class).in(Singleton.class);
        bind(new Key<CharacterContainer<Player>>(){}.getClass()).in(Singleton.class);
        bind(new Key<CharacterContainer<Enemy>>(){}.getClass()).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Waiter.InitialWaiterQueue
    Queue<Runnable> provideRunnableQueue() {
        return new LinkedList<Runnable>();
    }

    @Provides
    @Singleton
    @TerrainGrid.BackingTable
    Table<Integer, Integer, Terrain> providesBackingTable() {
        return HashBasedTable.create();
    }
}
