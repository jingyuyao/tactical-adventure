package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import javax.inject.Inject;

public class Heal extends Consumable {

  // TODO: Remove me
  static final int AMOUNT = 10;

  @Inject
  Heal(EventBus eventBus, @Assisted String name, @Assisted int usageLeft) {
    super(eventBus, name, usageLeft);
  }

  @Override
  protected void performConsumption(Player user) {
    user.healBy(AMOUNT);
  }
}
