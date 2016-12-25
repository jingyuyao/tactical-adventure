package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Set;

public abstract class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> passableTerrainTypes;
    private final List<Weapon> weapons;
    private int movementDistance;

    Character(
            int x,
            int y,
            String name,
            int movementDistance,
            Set<Terrain.Type> passableTerrainTypes,
            List<Weapon> weapons
    ) {
        super(x, y);
        this.name = name;
        this.movementDistance = movementDistance;
        this.passableTerrainTypes = passableTerrainTypes;
        this.weapons = weapons;
    }

    public String getName() {
        return name;
    }

    public Iterable<Weapon> getWeapons() {
        return Iterables.unmodifiableIterable(weapons);
    }

    public void moveTo(Coordinate newCoordinate, ImmutableList<Coordinate> path) {
        setCoordinate(newCoordinate);
        setChanged();
        notifyObservers(new Move(path));
    }

    public void instantMoveTo(Coordinate newCoordinate) {
        setCoordinate(newCoordinate);
        setChanged();
        notifyObservers(new InstantMove(newCoordinate));
    }

    boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    boolean canPassTerrainType(Terrain.Type terrainType) {
        return passableTerrainTypes.contains(terrainType);
    }

    int getMovementDistance() {
        return movementDistance;
    }

    void die() {
        setChanged();
        notifyObservers(new Dead());
        deleteObservers();
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", movementDistance=" + movementDistance +
                "} " + super.toString();
    }

    public static class Move {
        private final ImmutableList<Coordinate> path;

        Move(ImmutableList<Coordinate> path) {
            this.path = path;
        }

        public ImmutableList<Coordinate> getPath() {
            return path;
        }
    }

    public static class InstantMove {
        private final Coordinate destination;

        InstantMove(Coordinate destination) {
            this.destination = destination;
        }

        public Coordinate getDestination() {
            return destination;
        }
    }

    // TODO: Map to listen to this
    public static class Dead {
        Dead() {}
    }
}
