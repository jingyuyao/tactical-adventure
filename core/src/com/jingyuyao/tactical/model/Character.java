package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> canCrossTerrainTypes;
    private Character.Type type;
    private int movementDistance;
    private Collection<Terrain> lastPath;

    public Character(int x, int y, String name, Type type, int movementDistance) {
        super(x, y);
        this.name = name;
        this.type = type;
        this.movementDistance = movementDistance;
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

    public Collection<Terrain> getLastPath() {
        return lastPath;
    }

    Set<Terrain.Type> getCanCrossTerrainTypes() {
        return canCrossTerrainTypes;
    }

    int getMovementDistance() {
        return movementDistance;
    }

    void moveTo(int x, int y, Collection<Terrain> pathToCoordinate) {
        Preconditions.checkNotNull(pathToCoordinate);
        setPosition(x, y);
        lastPath = pathToCoordinate;
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
                ", type=" + type +
                ", movementDistance=" + movementDistance +
                "} " + super.toString();
    }
}
