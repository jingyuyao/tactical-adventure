package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.object.ObjectModule;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.state.StateModule;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;

public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new StateModule());
        install(new ObjectModule());
        install(new ItemModule());

        bind(Level.class);
        bind(Map.class);
        bind(AttackPlanFactory.class);
        bind(Highlighter.class);
        bind(MarkingFactory.class);
        bind(TargetInfoFactory.class);
        bind(TerrainGrid.class);
        bind(Turn.class);
        bind(Waiter.class);
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
