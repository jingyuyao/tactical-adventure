package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collection;

public class Weapon {
    private final Collection<Integer> attackDistances;

    public Weapon(Collection<Integer> attackDistances) {
        this.attackDistances = attackDistances;
    }

    public Collection<Integer> getAttackDistances() {
        return attackDistances;
    }

    // TODO: remove us
    public static Weapon oneDistanceWeapon() {
        Collection<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(1);

        return new Weapon(attackDistances);
    }

    public static Weapon threeDistanceRanged() {
        Collection<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(3);

        return new Weapon(attackDistances);
    }
}
