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
    private Character.Type type;
    private int totalMoveCost;
    private Path<Terrain> lastTerrainGraph;

    public Character(int x, int y, String name, Type type, int totalMoveCost) {
        super(x, y);
        this.name = name;
        this.type = type;
        this.totalMoveCost = totalMoveCost;
        canCrossTerrainTypes = createDefaultCanCrossTerrainTypes();
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        update();
    }

    public Path<Terrain> getLastPath() {
        return lastTerrainGraph;
    }

    Set<Terrain.Type> getCanCrossTerrainTypes() {
        return canCrossTerrainTypes;
    }

    int getTotalMoveCost() {
        return totalMoveCost;
    }

    void moveTo(int x, int y, Path<Terrain> pathToCoordinate) {
        Preconditions.checkNotNull(pathToCoordinate);
        setPosition(x, y);
        lastTerrainGraph = pathToCoordinate;
        update();
    }

    private static Set<Terrain.Type> createDefaultCanCrossTerrainTypes() {
        Set<Terrain.Type> standOnTerrainTypes = new HashSet<Terrain.Type>();
        standOnTerrainTypes.add(Terrain.Type.NORMAL);
        standOnTerrainTypes.add(Terrain.Type.OBSTRUCTED);
        return standOnTerrainTypes;
    }

    public enum Type {
        PLAYER, ENEMY
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
