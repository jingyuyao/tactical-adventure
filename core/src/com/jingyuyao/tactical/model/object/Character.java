package com.jingyuyao.tactical.model.object;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;

public abstract class Character extends AbstractObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Stats stats;
    private final Items items;

    Character(int x, int y, String name, Stats stats, Items items) {
        super(x, y);
        this.name = name;
        this.stats = stats;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Iterable<Weapon> getWeapons() {
        return items.getWeapons();
    }

    public Optional<Weapon> getEquippedWeapon() {
        return items.getEquippedWeapon();
    }

    public int getMoveDistance() {
        return stats.getMoveDistance();
    }

    public boolean isAlive() {
        return stats.getHp() > 0;
    }

    public boolean canTarget(Character other) {
        // TODO: make me more specific later
        return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
    }

    public boolean canPassTerrainType(Terrain.Type terrainType) {
        return stats.canPassTerrainType(terrainType);
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

    public void equipWeapon(Weapon weapon) {
        items.setEquippedWeapon(weapon);
    }

    public void damageBy(int delta) {
        boolean dead = stats.damageBy(delta);
        if (dead) {
            die();
        }
    }

    private void die() {
        died();
        setChanged();
        notifyObservers(new Died());
        deleteObservers();
    }

    /**
     * Called when this {@link Character} dies.
     */
    protected abstract void died();

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

    public static class Died {
        Died() {}
    }
}
