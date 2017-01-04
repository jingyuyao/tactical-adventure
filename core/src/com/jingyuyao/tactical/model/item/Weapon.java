package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;

import javax.inject.Inject;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status.
 * Not a {@link Consumable} since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Usable {
    private final int attackPower;
    private final ImmutableSet<Integer> attackDistances;

    @Inject
    Weapon(
            EventBus eventBus,
            @Assisted String name,
            @Assisted("usageLeft") int usageLeft,
            @Assisted("attackPower") int attackPower,
            @Assisted Iterable<Integer> attackDistances
    ) {
        super(eventBus, name, usageLeft);
        this.attackPower = attackPower;
        this.attackDistances = ImmutableSet.copyOf(attackDistances);
    }

    // TODO: this should be a function of a coordinate and terrains
    public ImmutableSet<Integer> getAttackDistances() {
        return attackDistances;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "attackPower=" + attackPower +
                ", attackDistances=" + attackDistances +
                "} " + super.toString();
    }
}
