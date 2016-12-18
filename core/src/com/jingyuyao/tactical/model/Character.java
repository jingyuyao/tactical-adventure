package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.graph.Path;

import java.util.HashSet;
import java.util.Set;

public class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> canCrossTerrainTypes;
    private int moveDistance;
    private Path<Terrain> lastTerrainGraph;

    Character(String name, int x, int y, int moveDistance) {
        super(x, y);
        this.name = name;
        this.moveDistance = moveDistance;
        canCrossTerrainTypes = createDefaultCanCrossTerrainTypes();
    }

    public String getName() {
        return name;
    }

    public Path<Terrain> getLastPath() {
        return lastTerrainGraph;
    }

    Set<Terrain.Type> getCanCrossTerrainTypes() {
        return canCrossTerrainTypes;
    }

    int getMoveDistance() {
        return moveDistance;
    }

    /**
     * @param pathToCoordinate The single path to the new coordinate
     */
    void moveTo(int x, int y, Path<Terrain> pathToCoordinate) {
        Preconditions.checkNotNull(pathToCoordinate);
        setX(x);
        setY(y);
        lastTerrainGraph = pathToCoordinate;
    }

    private static Set<Terrain.Type> createDefaultCanCrossTerrainTypes() {
        Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
        standOnTerrainTypes.add(Terrain.Type.NORMAL);
        standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
        return standOnTerrainTypes;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
