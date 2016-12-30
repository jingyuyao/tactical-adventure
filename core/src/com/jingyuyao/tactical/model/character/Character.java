package com.jingyuyao.tactical.model.character;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.event.InstantMove;
import com.jingyuyao.tactical.model.event.Move;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;

import java.util.List;

public abstract class Character extends MapObject {
    /**
     * Used for sprites and as ID.
     */
    private final String name;
    private final Stats stats;
    private final Items items;

    Character(EventBus eventBus, Coordinate coordinate, List<Marker> markers, String name, Stats stats, Items items) {
        super(eventBus, coordinate, markers);
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
            dispose();
        }
    }

    public void healBy(int delta) {
        stats.healBy(delta);
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", stats=" + stats +
                ", items=" + items +
                "} " + super.toString();
    }

}
