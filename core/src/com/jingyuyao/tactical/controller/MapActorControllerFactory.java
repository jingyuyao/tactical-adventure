package com.jingyuyao.tactical.controller;

import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.state.MapState;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapActorControllerFactory {
    private final MapState mapState;
    private final Highlighter highlighter;

    @Inject
    MapActorControllerFactory(MapState mapState, Highlighter highlighter) {
        this.mapState = mapState;
        this.highlighter = highlighter;
    }

    public MapActorController create(AbstractObject object, float size) {
        return new MapActorController(mapState, highlighter, object, size);
    }
}
