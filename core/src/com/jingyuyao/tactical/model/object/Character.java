package com.jingyuyao.tactical.model.object;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

public abstract class Character extends AbstractObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Stats stats;
    private final Items items;

    Character(EventBus eventBus, int x, int y, String name, Stats stats, Items items) {
        super(eventBus, x, y);
        this.name = name;
        this.stats = stats;
        this.items = items;
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
        getEventBus().post(new Move(this, path));
    }

    public void instantMoveTo(Coordinate newCoordinate) {
        setCoordinate(newCoordinate);
        getEventBus().post(new InstantMove(this, newCoordinate));
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
        getEventBus().post(new Died(this));
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
        private final Character character;
        private final ImmutableList<Coordinate> path;

        private Move(Character character, ImmutableList<Coordinate> path) {
            this.character = character;
            this.path = path;
        }

        public Character getCharacter() {
            return character;
        }

        public ImmutableList<Coordinate> getPath() {
            return path;
        }
    }

    public static class InstantMove {
        private final Character character;
        private final Coordinate destination;

        private InstantMove(Character character, Coordinate destination) {
            this.character = character;
            this.destination = destination;
        }

        public Character getCharacter() {
            return character;
        }

        public Coordinate getDestination() {
            return destination;
        }
    }

    public static class Died {
        private final Character character;

        private Died(Character character) {
            this.character = character;
        }

        public Character getCharacter() {
            return character;
        }
    }
}
