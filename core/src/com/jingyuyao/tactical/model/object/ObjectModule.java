package com.jingyuyao.tactical.model.object;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.mark.Marker;

import java.util.ArrayList;
import java.util.List;

public class ObjectModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectFactory.class);
    }

    @Provides
    @MapObject.InitialMarkers
    List<Marker> provideMarkers() {
        return new ArrayList<Marker>();
    }
}
