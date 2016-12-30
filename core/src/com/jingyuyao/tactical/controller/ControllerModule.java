package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapActorControllerFactory.class);
        bind(MapController.class);
    }
}
