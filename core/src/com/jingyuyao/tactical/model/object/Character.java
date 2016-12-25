package com.jingyuyao.tactical.model.object;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.item.Items;

public abstract class Character extends AbstractObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Stats stats;
    private final Items items;

    Character(
            int x,
            int y,
            String name,
            Stats stats,
            Items items
    ) {
        super(x, y);
        this.name = name;
        this.stats = stats;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Items getItems() {
        return items;
    }

    public Stats getStats() {
        return stats;
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

    public boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", stats=" + stats +
                ", items=" + items +
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
