package com.jingyuyao.tactical.model.state;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.mark.Marking;

import javax.inject.Singleton;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StateModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(StateFactory.class));
        bind(MapState.class);
        bind(Markings.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    @Markings.InitialDangerAreas
    Map<Character, Marking> provideInitialDangerAreas() {
        return new HashMap<Character, Marking>();
    }

    @Provides
    @Singleton
    @MapState.BackingStateStack
    Deque<State> provideStateStack() {
        return new LinkedList<State>();
    }
}
