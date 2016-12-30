package com.jingyuyao.tactical.view;

import com.google.inject.AbstractModule;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LevelScreenFactory.class);
        bind(MapViewFactory.class);
        bind(MapUIFactory.class);
    }
}
