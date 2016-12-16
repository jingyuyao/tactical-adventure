package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ControllerFactory.class).in(Singleton.class);
    }
}
