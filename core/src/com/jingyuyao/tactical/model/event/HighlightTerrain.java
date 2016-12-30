package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.object.Terrain;

public class HighlightTerrain implements ModelEvent {
    private final Terrain terrain;

    public HighlightTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}
