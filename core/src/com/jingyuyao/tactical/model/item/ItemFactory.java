package com.jingyuyao.tactical.model.item;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.TargetFactory;

public interface ItemFactory {

  Weapon createWeapon(
      String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      TargetFactory targetFactory);

  Heal createHeal(String name, int usageLeft);
}
