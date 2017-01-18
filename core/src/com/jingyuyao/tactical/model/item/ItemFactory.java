package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public interface ItemFactory {

  @Named("Melee")
  Weapon createMelee(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower);

  @Named("PiercingLaser")
  Weapon createPiercingLaser(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower);

  @Named("Heal")
  Consumable createHeal(String name, int usageLeft);
}
