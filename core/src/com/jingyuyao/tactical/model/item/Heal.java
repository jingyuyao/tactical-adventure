package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;

class Heal extends BaseItem implements Consumable {

  // TODO: Remove me
  static final int AMOUNT = 10;

  @Inject
  Heal(EventBus eventBus, @Assisted String name, @Assisted int usageLeft) {
    super(eventBus, name, usageLeft);
  }

  @Override
  public void consume(Character user) {
    user.healBy(AMOUNT);
    useOnce();
  }
}
