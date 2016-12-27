package com.jingyuyao.tactical.model.object;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.Algorithms;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.HashSet;
import java.util.Set;

public class Terrain extends AbstractObject {
    private final Set<Markers> markers;
    private Type type;

    public Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
        markers = new HashSet<Markers>();
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    @Override
    public void highlight(Highlighter highlighter) {
        highlighter.highlight(this);
    }

    public Type getType() {
        return type;
    }

    public void addMarker(Markers marker) {
        markers.add(marker);
        setChanged();
        notifyObservers(new MarkerChange(markers));
    }

    public void clearMarkers() {
        markers.clear();
        setChanged();
        notifyObservers(new MarkerChange(markers));
    }

    public int getMovementPenalty(Character character) {
        if (!character.canPassTerrainType(type)) {
            return Algorithms.NO_EDGE;
        }

        switch (type) {
            case OBSTRUCTED:
                return 2;
            case WATER:
            case MOUNTAIN:
                return 3;
            case NORMAL:
            default:
                return 1;
        }
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "markers=" + markers +
                ", type=" + type +
                "} " + super.toString();
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    public static class MarkerChange {
        private final ImmutableSet<Markers> markers;

        private MarkerChange(Set<Markers> markers) {
            this.markers = ImmutableSet.copyOf(markers);
        }

        public ImmutableSet<Markers> getMarkers() {
            return markers;
        }
    }
}
