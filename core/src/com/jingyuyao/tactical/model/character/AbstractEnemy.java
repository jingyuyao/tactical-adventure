package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;

abstract class AbstractEnemy extends AbstractCharacter<CharacterData> implements Enemy {

  AbstractEnemy(
      CharacterData data,
      Multiset<Marker> markers,
      List<Item> items,
      EventBus eventBus,
      TerrainGraphs terrainGraphs,
      Characters characters) {
    super(data, markers, items, eventBus, terrainGraphs, characters);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }
}
