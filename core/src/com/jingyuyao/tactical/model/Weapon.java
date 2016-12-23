package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collection;

public class Weapon {
    private final Collection<Integer> attackDistances;
    private final String name;

    public Weapon(String name, Collection<Integer> attackDistances) {
        this.name = name;
        this.attackDistances = attackDistances;
    }

    public Collection<Integer> getAttackDistances() {
        return attackDistances;
    }

    public String getName() {
        return name;
    }

    // TODO: remove us
    public static Weapon oneDistanceWeapon() {
        Collection<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(1);

        return new Weapon("Axe", attackDistances);
    }

    public static Weapon threeDistanceRanged() {
        Collection<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(3);

        return new Weapon("Long bow", attackDistances);
    }
}
