package com.jingyuyao.tactical.model;

import java.util.HashSet;
import java.util.Set;

public class Terrain extends MapObject {
    private final Set<Marker> markers;
    private Type type;

    public Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
        markers = new HashSet<Marker>();
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    int getMovementPenalty(Character character) {
        if (!character.getCanCrossTerrainTypes().contains(type)) {
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

    void addMarker(Marker marker) {
        markers.add(marker);
        update();
    }

    void clearMarkers() {
        markers.clear();
        update();
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

    @Override
    public String toString() {
        return "Terrain{" +
                "markers=" + markers +
                ", type=" + type +
                "} " + super.toString();
    }
}
