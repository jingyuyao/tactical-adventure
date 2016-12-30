package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.object.MapObject;

public class RemoveMarker implements ModelEvent {
    private final MapObject object;
    private final Marker marker;

    public RemoveMarker(MapObject object, Marker marker) {
        this.object = object;
        this.marker = marker;
    }

    public MapObject getObject() {
        return object;
    }

    public Marker getMarker() {
        return marker;
    }
}
