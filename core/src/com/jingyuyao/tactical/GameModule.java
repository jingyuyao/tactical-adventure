package com.jingyuyao.tactical;

import com.google.inject.AbstractModule;

/**
 * Simple module to bind {@link TacticalAdventure}
 */
public class GameModule extends AbstractModule {
    private final TacticalAdventure game;

    GameModule(TacticalAdventure game) {
        this.game = game;
    }

    @Override
    protected void configure() {
        bind(TacticalAdventure.class).toInstance(game);
    }
}
