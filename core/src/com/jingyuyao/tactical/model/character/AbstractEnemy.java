package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter implements Enemy {

  AbstractEnemy(EventBus eventBus) {
    super(eventBus);
  }

  AbstractEnemy(
      EventBus eventBus, String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    super(eventBus, name, maxHp, hp, moveDistance, items);
  }
}
