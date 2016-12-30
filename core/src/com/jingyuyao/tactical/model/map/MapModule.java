package com.jingyuyao.tactical.model.map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.mark.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @com.jingyuyao.tactical.model.map.MapObject.InitialMarkers
    List<Marker> provideMarkers() {
        return new ArrayList<Marker>();
    }
}
