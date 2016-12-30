package com.jingyuyao.tactical.view;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.view.actor.ActorModule;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ActorModule());

        bind(LevelScreenFactory.class);
        bind(MapViewFactory.class);
        bind(MapUIFactory.class);
    }
}
