package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting.EndTurn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * A player character
 */
public class Player extends Character {

  private boolean actionable = true;
  private Marking marking = null;

  @Inject
  Player(
      EventBus eventBus,
      @Assisted Coordinate coordinate,
      @InitialMarkers List<Marker> markers,
      @Assisted String name,
      @Assisted Stats stats,
      @Assisted Items items,
      TargetsFactory targetsFactory) {
    super(eventBus, coordinate, markers, name, stats, items, targetsFactory);
    register();
  }

  @Subscribe
  public void endTurn(EndTurn endTurn) {
    setActionable(true);
  }

  @Override
  public void dispose() {
    clearMarking();
    unregister();
    super.dispose();
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

  public void showMoves() {
    Preconditions.checkState(marking == null);

    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : createTargets().moveTerrains()) {
      markerMap.put(terrain, Marker.CAN_MOVE_TO);
    }
    marking = new Marking(markerMap);
    marking.apply();
  }

  public void clearMarking() {
    if (marking != null) {
      marking.clear();
      marking = null;
    }
  }
}
