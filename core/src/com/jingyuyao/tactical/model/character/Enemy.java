package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;

/**
 * An enemy character
 */
public class Enemy extends Character {

  private final Retaliation retaliation;

  /**
   * Retaliation type is supplied by child class.
   */
  Enemy(
      EventBus eventBus,
      Coordinate coordinate,
      Stats stats,
      List<Item> items,
      Retaliation retaliation) {
    super(eventBus, coordinate, stats, items);
    this.retaliation = retaliation;
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public ListenableFuture<Void> retaliate() {
    return retaliation.run(this);
  }
}
