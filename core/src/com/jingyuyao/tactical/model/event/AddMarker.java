package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

public class AddMarker implements ModelEvent {
    private final MapObject object;
    private final Marker marker;

    public AddMarker(MapObject object, Marker marker) {
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
