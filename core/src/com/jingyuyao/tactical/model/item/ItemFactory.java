package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;

public interface ItemFactory {
    Weapon createWeapon(String name, @Assisted("usageLeft") int usageLeft, @Assisted("attackPower") int attackPower, Iterable<Integer> attackDistances);

    Heal createHeal(String name, int usageLeft);
}
