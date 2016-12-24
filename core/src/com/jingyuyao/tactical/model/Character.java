package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> canCrossTerrainTypes;
    private final List<Weapon> weapons;
    private int movementDistance;

    public Character(int x, int y, String name, int movementDistance) {
        super(x, y);
        this.name = name;
        this.movementDistance = movementDistance;
        canCrossTerrainTypes = createDefaultCanCrossTerrainTypes();
        // TODO: remove me
        weapons = new ArrayList<Weapon>();
        weapons.add(Weapon.oneDistanceWeapon());
        weapons.add(Weapon.threeDistanceRanged());
    }

    public String getName() {
        return name;
    }

    public ImmutableList<Weapon> getWeapons() {
        return ImmutableList.copyOf(weapons);
    }

    boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    ImmutableSet<Terrain.Type> getCanCrossTerrainTypes() {
        return ImmutableSet.copyOf(canCrossTerrainTypes);
    }

    int getMovementDistance() {
        return movementDistance;
    }

    public void moveTo(Coordinate newCoordinate, ImmutableList<Coordinate> path) {
        setCoordinate(newCoordinate);
        setChanged();
        notifyObservers(new PositionUpdate(path));
    }

    void die() {
        setChanged();
        notifyObservers(new Dead());
        deleteObservers();
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

    public static class PositionUpdate {
        private final ImmutableList<Coordinate> path;

        PositionUpdate(ImmutableList<Coordinate> path) {
            this.path = path;
        }

        public ImmutableList<Coordinate> getPath() {
            return path;
        }
    }

    // oh no
    public static class Dead {}
}
