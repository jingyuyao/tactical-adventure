package com.jingyuyao.tactical.model.object;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Marker;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

import java.util.HashMap;
import java.util.Map;

public abstract class Character extends AbstractObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Stats stats;
    private final Items items;
    /**
     * The map of markers create by/for this {@link Character}.
     */
    private final Map<Coordinate, Marker> terrainMarkers;

    Character(int x, int y, String name, Stats stats, Items items) {
        super(x, y);
        this.name = name;
        this.stats = stats;
        this.items = items;
        terrainMarkers = new HashMap<Coordinate, Marker>();
    }

    @Override
    public void highlight(Highlighter highlighter) {
        highlighter.highlight(this);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return stats.getHp();
    }

    public Iterable<Consumable> getConsumables() {
        return items.getConsumables();
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

    public Map<Coordinate, Marker> getTerrainMarkers() {
        return terrainMarkers;
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

    public void setMarkerMode(MarkerMode newMarkerMode) {
        setChanged();
        notifyObservers(new MarkerModeChange(newMarkerMode));
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

    public void healBy(int delta) {
        stats.healBy(delta);
    }

    private void die() {
        setMarkerMode(MarkerMode.NONE);
        setChanged();
        notifyObservers(new Died());
        deleteObservers();
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", stats=" + stats +
                ", items=" + items +
                "} " + super.toString();
    }

    public enum MarkerMode {
        NONE,
        MOVE_AND_TARGETS,
        IMMEDIATE_TARGETS,
        DANGER,
    }

    public static class Move {
        private final ImmutableList<Coordinate> path;

        private Move(ImmutableList<Coordinate> path) {
            this.path = path;
        }

        public ImmutableList<Coordinate> getPath() {
            return path;
        }
    }

    public static class InstantMove {
        private final Coordinate destination;

        private InstantMove(Coordinate destination) {
            this.destination = destination;
        }

        public Coordinate getDestination() {
            return destination;
        }
    }

    public static class Died {
        private Died() {}
    }

    public static class MarkerModeChange {
        /**
         * Need to store the new marker mode in the params object since we run these things in
         * a runnable, which means we can fall to the good o js loop reference bug
         */
        private final MarkerMode newMarkerMode;

        private MarkerModeChange(MarkerMode newMarkerMode) {
            this.newMarkerMode = newMarkerMode;
        }

        public MarkerMode getNewMarkerMode() {
            return newMarkerMode;
        }
    }
}
