package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> canCrossTerrainTypes;
    private final Collection<Weapon> weapons;
    private int movementDistance;
    private Collection<Terrain> lastPath;
    private boolean dead;

    public Character(int x, int y, String name, int movementDistance) {
        super(x, y);
        this.name = name;
        this.movementDistance = movementDistance;
        canCrossTerrainTypes = createDefaultCanCrossTerrainTypes();
        dead = false;
        // TODO: remove me
        weapons = new ArrayList<Weapon>();
        weapons.add(Weapon.oneDistanceWeapon());
        weapons.add(Weapon.threeDistanceRanged());
    }

    public String getName() {
        return name;
    }

    public Collection<Weapon> getWeapons() {
        return weapons;
    }

    public Collection<Terrain> getLastPath() {
        return lastPath;
    }

    public boolean isDead() {
        return dead;
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
        setChanged();
        notifyObservers();
    }

    void die() {
        // oh no
        dead = true;
        setChanged();
        notifyObservers();
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
                ", movementDistance=" + movementDistance +
                "} " + super.toString();
    }
}
