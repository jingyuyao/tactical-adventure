package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

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
    private ImmutableList<Coordinate> lastPath;
    private int movementDistance;

    private boolean dead;

    public Character(int x, int y, String name, int movementDistance) {
        super(x, y);
        this.name = name;
        this.movementDistance = movementDistance;
        lastPath = ImmutableList.of();
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

    public ImmutableList<Coordinate> getLastPath() {
        return lastPath;
    }

    public boolean isDead() {
        return dead;
    }

    boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    Set<Terrain.Type> getCanCrossTerrainTypes() {
        return canCrossTerrainTypes;
    }

    int getMovementDistance() {
        return movementDistance;
    }

    void moveTo(int x, int y, ImmutableList<Coordinate> pathToCoordinate) {
        Preconditions.checkNotNull(pathToCoordinate);
        lastPath = pathToCoordinate;
        setPosition(x, y);
    }

    /**
     * Moves the {@code character} back to its previous starting point.
     */
    public void moveBack() {
        if (lastPath.size() > 1) {
            Coordinate previousCoordinate = lastPath.iterator().next();
            if (!previousCoordinate.equals(getCoordinate())) {
                moveTo(previousCoordinate.getX(), previousCoordinate.getY(), lastPath.reverse());
            }
        }
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
