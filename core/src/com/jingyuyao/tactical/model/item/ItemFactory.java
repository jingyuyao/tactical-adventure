package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import java.util.Set;

public interface ItemFactory {

  Weapon createWeapon(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      Set<Integer> attackDistances);

  Heal createHeal(String name, int usageLeft);
}
