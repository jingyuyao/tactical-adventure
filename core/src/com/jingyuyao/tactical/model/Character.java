package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

public class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private int moveDistance = 5; // Hard code for now
    private TerrainPath lastTerrainGraph;

    Character(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TerrainPath getLastPath() {
        return lastTerrainGraph;
    }

    int getMoveDistance() {
        return moveDistance;
    }

    /**
     * @param pathToCoordinate The single path to the new coordinate
     */
    void moveTo(int x, int y, TerrainPath pathToCoordinate) {
        Preconditions.checkNotNull(pathToCoordinate);
        setX(x);
        setY(y);
        lastTerrainGraph = pathToCoordinate;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
