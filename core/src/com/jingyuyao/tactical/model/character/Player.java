package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

/**
 * A player character
 */
public class Player extends Character {

  private boolean actionable = true;

  @Inject
  Player(
      EventBus eventBus,
      @Assisted Coordinate coordinate,
      @Assisted Stats stats,
      @Assisted List<Item> items) {
    super(eventBus, coordinate, stats, items);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public boolean isActionable() {
    return actionable;
  }

  public void setActionable(boolean actionable) {
    this.actionable = actionable;
    post(new NewActionState(this, actionable));
  }
}
