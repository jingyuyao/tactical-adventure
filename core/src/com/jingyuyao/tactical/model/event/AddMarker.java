package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.object.AbstractObject;

public class AddMarker implements ModelEvent {
    private final AbstractObject object;
    private final Marker marker;

    public AddMarker(AbstractObject object, Marker marker) {
        this.object = object;
        this.marker = marker;
    }

    public AbstractObject getObject() {
        return object;
    }

    public Marker getMarker() {
        return marker;
    }
}
