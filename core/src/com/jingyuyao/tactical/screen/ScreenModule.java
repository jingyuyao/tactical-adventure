package com.jingyuyao.tactical.screen;

import com.google.inject.AbstractModule;

public class ScreenModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameScreenFactory.class);
    }
}
