package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Inject;

class Heal extends BaseItem<ItemStats> implements Consumable {

  // TODO: Remove me
  static final int AMOUNT = 10;

  @Inject
  Heal(EventBus eventBus, @Assisted ItemStats itemStats) {
    super(eventBus, itemStats);
  }

  @Override
  public void consume(Character user) {
    useOnce();
    user.healBy(AMOUNT);
  }
}
