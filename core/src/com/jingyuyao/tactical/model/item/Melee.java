package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.BattleModule.Immediate;
import com.jingyuyao.tactical.model.battle.TargetFactory;
import javax.inject.Inject;

public class Melee extends Weapon {

  @Inject
  Melee(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower,
      @Immediate TargetFactory targetFactory) {
    super(eventBus, name, usageLeft, attackPower, targetFactory);
  }
}
