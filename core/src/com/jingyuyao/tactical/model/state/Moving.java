package com.jingyuyao.tactical.model.state;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

class Moving extends AbstractMovementState {

  private final Movement movement;
  private Coordinate previousCoordinate;

  @Inject
  Moving(
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Movement movement) {
    super(mapState, stateFactory, player);
    this.movement = movement;
  }

  @Override
  public void enter() {
    super.enter();
    movement.showMarking();
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
    movement.hideMarking();
  }

  @Override
  public void select(Terrain terrain) {
    if (movement.canMoveTo(terrain.getCoordinate())) {
      Path path = movement.pathTo(terrain.getCoordinate());
      previousCoordinate = getPlayer().getCoordinate();
      Futures.addCallback(getPlayer().moveAlong(path), new FutureCallback<Void>() {
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
