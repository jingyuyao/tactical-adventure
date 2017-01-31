package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter implements Enemy {

  AbstractEnemy(Multiset<Marker> markers, EventBus eventBus, Characters characters) {
    super(markers, eventBus, characters);
  }

  AbstractEnemy(
      Coordinate coordinate, Multiset<Marker> markers, Characters characters, EventBus eventBus,
      String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    super(coordinate, markers, characters, eventBus, name, maxHp, hp, moveDistance, items);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }
}
