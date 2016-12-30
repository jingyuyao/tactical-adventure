package com.jingyuyao.tactical.model.state;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.Marking;
import com.jingyuyao.tactical.model.object.Character;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class StateModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapState.class);
        bind(State.class).annotatedWith(MapState.InitialState.class).to(Waiting.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Markings.InitialDangerAreas
    Map<Character, Marking> provideInitialDangerAreas() {
        return new HashMap<Character, Marking>();
    }
}
