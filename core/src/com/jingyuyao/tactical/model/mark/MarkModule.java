package com.jingyuyao.tactical.model.mark;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class MarkModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MarkingFactory.class);
        bind(Markings.class);
    }

    @Provides
    @Singleton
    @com.jingyuyao.tactical.model.mark.Markings.InitialDangerAreas
    Map<Character, Marking> provideInitialDangerAreas() {
        return new HashMap<Character, Marking>();
    }
}
