package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;

/**
 * An enemy character
 */
public abstract class Enemy extends Character<CharacterData> {

  Enemy(
      Coordinate coordinate,
      Multiset<Marker> markers,
      CharacterData data,
      List<Item> items,
      EventBus eventBus,
      TerrainGraphs terrainGraphs,
      Characters characters) {
    super(coordinate, markers, data, items, eventBus, terrainGraphs, characters);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public abstract ListenableFuture<Void> retaliate();
}
