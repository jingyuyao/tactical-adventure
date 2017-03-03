package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter implements Enemy {

  AbstractEnemy(EventBus eventBus, Terrains terrains) {
    super(eventBus, terrains);
  }

  AbstractEnemy(
      Coordinate coordinate, EventBus eventBus, Terrains terrains, String name, int maxHp, int hp,
      int moveDistance, List<Item> items) {
    super(coordinate, eventBus, terrains, name, maxHp, hp, moveDistance, items);
  }

  @Override
  public void select(SelectionHandler selectionHandler) {
    selectionHandler.select(this);
  }
}
