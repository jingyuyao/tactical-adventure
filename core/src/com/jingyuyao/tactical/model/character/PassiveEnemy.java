package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.retaliation.PassiveRetaliation;
import java.util.List;
import javax.inject.Inject;

class PassiveEnemy extends Enemy {

  @Inject
  PassiveEnemy(EventBus eventBus,
      @Assisted Coordinate coordinate,
      @Assisted Stats stats,
      @Assisted List<Item> items,
      PassiveRetaliation retaliation) {
    super(eventBus, coordinate, stats, items, retaliation);
  }
}
