package com.jingyuyao.tactical.model.mark;

import com.google.inject.AbstractModule;

public class MarkModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MarkingFactory.class);
    }
}
