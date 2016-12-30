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
        bind(CharacterContainer.class);
    }

    @Provides
    @Singleton
    @CharacterContainer.InitialCharacterSet
    Set<Character> provideInitialCharacterSet() {
        return new HashSet<Character>();
    }
}
