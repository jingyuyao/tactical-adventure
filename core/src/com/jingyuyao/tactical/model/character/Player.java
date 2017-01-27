package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.character.CharacterModule.InitialItems;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

/**
 * A player character
 */
public class Player extends AbstractCharacter<PlayerData> {

  @Inject
  Player(
      @Assisted Coordinate coordinate,
      @InitialMarkers Multiset<Marker> markers,
      @Assisted PlayerData data,
      @InitialItems List<Item> items,
      @CharacterEventBus EventBus eventBus,
      TerrainGraphs terrainGraphs,
      Characters characters) {
    super(coordinate, markers, data, items, eventBus, terrainGraphs, characters);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  public boolean isActionable() {
    return getData().isActionable();
  }

  public void setActionable(boolean actionable) {
    getData().setActionable(actionable);
  }
}
