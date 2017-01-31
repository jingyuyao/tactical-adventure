package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

public class BasePlayer extends AbstractCharacter implements Player {

  private boolean actionable;

  @Inject
  BasePlayer(
      @InitialMarkers Multiset<Marker> markers,
      @CharacterEventBus EventBus eventBus,
      Characters characters) {
    super(markers, eventBus, characters);
  }

  BasePlayer(
      Coordinate coordinate, Multiset<Marker> markers, Characters characters, EventBus eventBus,
      String name, int maxHp, int hp, int moveDistance, List<Item> items, boolean actionable) {
    super(coordinate, markers, characters, eventBus, name, maxHp, hp, moveDistance,
        items);
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
