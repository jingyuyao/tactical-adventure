package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;
import java.util.HashSet;

public class ObjectModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectFactory.class);
    }

    @Provides
    @Singleton
    CharacterContainer<Player> providePlayerContainer(EventBus eventBus) {
        return new CharacterContainer<Player>(eventBus, new HashSet<Player>());
    }

    @Provides
    @Singleton
    CharacterContainer<Enemy> provideEnemyContainer(EventBus eventBus) {
        return new CharacterContainer<Enemy>(eventBus, new HashSet<Enemy>());
    }
}
