package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;

public interface ItemFactory {

  Melee createMelee(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower);

  PiercingLaser createPiercingLaser(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower);

  Heal createHeal(String name, int usageLeft);
}
