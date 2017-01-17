package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

class Moving extends AbstractPlayerState {

  private final MovementFactory movementFactory;
  private Coordinate previousCoordinate;
  private Marking marking;

  @Inject
  Moving(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      MovementFactory movementFactory) {
    super(eventBus, mapState, stateFactory, player);
    this.movementFactory = movementFactory;
  }

  @Override
  public void enter() {
    super.enter();
    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : movementFactory.create(getPlayer()).getTerrains()) {
      markerMap.put(terrain, Marker.CAN_MOVE_TO);
    }
    marking = new Marking(markerMap);
    marking.apply();
  }

  @Override
  public void canceled() {
    if (previousCoordinate != null) {
      getPlayer().instantMoveTo(previousCoordinate);
      previousCoordinate = null;
    }
  }

  @Override
  public void exit() {
    marking.clear();
    marking = null;
  }

  @Override
  public void select(Player player) {
    if (getPlayer().equals(player)) {
      goTo(getStateFactory().createMoved(player));
    } else {
      rollback();
      goTo(getStateFactory().createMoving(player));
    }
  }

  @Override
  public void select(Terrain terrain) {
    Movement playerMovement = movementFactory.create(getPlayer());
    if (playerMovement.canMoveTo(terrain.getCoordinate())) {
      Path path = playerMovement.pathTo(terrain.getCoordinate());
      previousCoordinate = getPlayer().getCoordinate();
      Futures.addCallback(getPlayer().move(path), new FutureCallback<Void>() {
        @Override
        public void onSuccess(Void result) {
          goTo(getStateFactory().createMoved(getPlayer()));
        }

        @Override
        public void onFailure(Throwable t) {

        }
      });
    } else {
      // we will consider clicking outside of movable area to be canceling
      back();
    }
  }
}
