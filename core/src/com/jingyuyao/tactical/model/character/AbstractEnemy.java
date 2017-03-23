package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter implements Enemy {

  AbstractEnemy(EventBus eventBus) {
    super(eventBus);
  }

  AbstractEnemy(
      Coordinate coordinate, EventBus eventBus, String name, int maxHp, int hp,
      int moveDistance, List<Item> items) {
    super(coordinate, eventBus, name, maxHp, hp, moveDistance, items);
  }
}
