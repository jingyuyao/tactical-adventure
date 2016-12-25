package com.jingyuyao.tactical.model.object;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.item.Items;

import java.util.Set;

public abstract class Character extends AbstractObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Set<Terrain.Type> passableTerrainTypes;
    private final Items items;
    private int movementDistance;

    Character(
            int x,
            int y,
            String name,
            int movementDistance,
            Set<Terrain.Type> passableTerrainTypes,
            Items items
    ) {
        super(x, y);
        this.name = name;
        this.movementDistance = movementDistance;
        this.passableTerrainTypes = passableTerrainTypes;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Items getItems() {
        return items;
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

    public void die() {
        setChanged();
        notifyObservers(new Dead());
        deleteObservers();
    }

    public int getMovementDistance() {
        return movementDistance;
    }

    public boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    boolean canPassTerrainType(Terrain.Type terrainType) {
        return passableTerrainTypes.contains(terrainType);
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

    public static class Dead {
        Dead() {}
    }
}
