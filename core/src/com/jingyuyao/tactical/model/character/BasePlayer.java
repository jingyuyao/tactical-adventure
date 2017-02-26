package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

public class BasePlayer extends AbstractCharacter implements Player {

  private boolean actionable;

  @Inject
  BasePlayer(@CharacterEventBus EventBus eventBus, Terrains terrains) {
    super(eventBus, terrains);
  }

  BasePlayer(
      Coordinate coordinate, EventBus eventBus, Terrains terrains, String name, int maxHp, int hp,
      int moveDistance, List<Item> items, boolean actionable) {
    super(coordinate, eventBus, terrains, name, maxHp, hp, moveDistance, items);
    this.actionable = actionable;
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  @Override
  public boolean isActionable() {
    return actionable;
  }

  @Override
  public void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}
