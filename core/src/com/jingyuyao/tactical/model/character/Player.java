package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.character.CharacterModule.InitialItems;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
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
      @Assisted Coordinate coordinate,
      @InitialMarkers Multiset<Marker> markers,
      TerrainGraphs terrainGraphs,
      Characters characters,
      @CharacterEventBus EventBus eventBus,
      @Assisted Stats stats,
      @InitialItems List<Item> items) {
    super(coordinate, markers, terrainGraphs, characters, eventBus, stats, items);
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
  }
}
