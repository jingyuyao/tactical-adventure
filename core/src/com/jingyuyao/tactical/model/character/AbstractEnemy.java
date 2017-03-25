package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter implements Enemy {

  AbstractEnemy() {
  }

  AbstractEnemy(String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    super(name, maxHp, hp, moveDistance, items);
  }
}
