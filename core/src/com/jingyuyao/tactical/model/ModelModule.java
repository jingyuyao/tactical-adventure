package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.ObjectModule;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.map.MapModule;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.MarkModule;
import com.jingyuyao.tactical.model.state.StateModule;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new MapModule());
        install(new ObjectModule());
        install(new ItemModule());
        install(new MarkModule());
        install(new StateModule());

        bind(CharacterContainer.class);
        bind(TerrainGrid.class);
        bind(Waiter.class);
        bind(Highlighter.class);
        bind(AttackPlanFactory.class);
        bind(TargetInfoFactory.class);
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

    @Provides
    @Singleton
    @CharacterContainer.InitialCharacterSet
    Set<Character> provideInitialCharacterSet() {
        return new HashSet<Character>();
    }
}
