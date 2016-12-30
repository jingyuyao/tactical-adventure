package com.jingyuyao.tactical.model.object;

import com.google.inject.AbstractModule;

public class ObjectModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectFactory.class);
    }
}
