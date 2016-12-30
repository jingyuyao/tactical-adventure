package com.jingyuyao.tactical.model.object;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

public class ObjectModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectFactory.class);
        bind(PlayerContainer.class);
        bind(EnemyContainer.class);
    }

    @Provides
    @Singleton
    @PlayerContainer.InitialPlayerSet
    Set<Player> provideInitialPlayerSet() {
        return new HashSet<Player>();
    }

    @Provides
    @Singleton
    @EnemyContainer.InitialEnemySet
    Set<Enemy> provideInitialEnemySet() {
        return new HashSet<Enemy>();
    }
}
