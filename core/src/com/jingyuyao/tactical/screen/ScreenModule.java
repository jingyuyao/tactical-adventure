package com.jingyuyao.tactical.screen;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ScreenModule extends AbstractModule {
    @Override
    protected void configure() {
        // Can be singleton since TacticalAdventure is singleton
        bind(GameScreenFactory.class).in(Singleton.class);
    }
}
