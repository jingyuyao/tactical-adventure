package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private final List<Integer> attackDistances;
    private final String name;

    public Weapon(String name, List<Integer> attackDistances) {
        this.name = name;
        this.attackDistances = attackDistances;
    }

    public ImmutableList<Integer> getAttackDistances() {
        return ImmutableList.copyOf(attackDistances);
    }

    public String getName() {
        return name;
    }

    // TODO: remove us
    public static Weapon oneDistanceWeapon() {
        List<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(1);

        return new Weapon("Axe", attackDistances);
    }

    public static Weapon threeDistanceRanged() {
        List<Integer> attackDistances = new ArrayList<Integer>();
        attackDistances.add(3);

        return new Weapon("Long bow", attackDistances);
    }
}
