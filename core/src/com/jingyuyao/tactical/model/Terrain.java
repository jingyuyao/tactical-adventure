package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends MapObject {
    private final List<Marker> markers;
    private Type type;

    public Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
        markers = new ArrayList<Marker>();
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public void addMarker(Marker marker) {
        markers.add(marker);
        setChanged();
        notifyObservers(new MarkerChange(markers));
    }

    public void clearMarkers() {
        markers.clear();
        setChanged();
        notifyObservers(new MarkerChange(markers));
    }

    int getMovementPenalty(Character character) {
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

    /**
     * Visual marker like for things like move target or danger area
     */
    public enum Marker {
        MOVE,
        ATTACK,
        DANGER,
    }

    public static class MarkerChange {
        private final ImmutableList<Marker> markers;

        MarkerChange(List<Marker> markers) {
            this.markers = ImmutableList.copyOf(markers);
        }

        public ImmutableList<Marker> getMarkers() {
            return markers;
        }
    }
}
