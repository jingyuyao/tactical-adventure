package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;

/**
 * An enemy character
 */
public abstract class Enemy extends Character {

  Enemy(EventBus eventBus, Coordinate coordinate, Stats stats, List<Item> items) {
    super(eventBus, coordinate, stats, items);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public abstract ListenableFuture<Void> retaliate();
}
