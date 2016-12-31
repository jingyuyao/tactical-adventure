package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

public class RemoveMarker extends ObjectEvent<MapObject> {
    private final Marker marker;

    public RemoveMarker(MapObject object, Marker marker) {
        super(object);
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }
}
