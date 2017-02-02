package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HideMovement;
import com.jingyuyao.tactical.model.event.ShowMovement;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

class Moving extends ItemSelectionState {

  private final EventBus eventBus;
  private final Movement movement;
  private Coordinate previousCoordinate;

  @Inject
  Moving(
      MapState mapState,
      StateFactory stateFactory,
      Movements movements,
      @ModelEventBus EventBus eventBus,
      @Assisted Player player,
      @Assisted Movement movement) {
    super(mapState, stateFactory, movements, player);
    this.eventBus = eventBus;
    this.movement = movement;
  }

  @Override
  public void enter() {
    super.enter();
    eventBus.post(new ShowMovement(movement));
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
    eventBus.post(new HideMovement(movement));
  }

  @Override
  public void select(Terrain terrain) {
    if (movement.canMoveTo(terrain.getCoordinate())) {
      Path path = movement.pathTo(terrain.getCoordinate());
      previousCoordinate = getPlayer().getCoordinate();
      goTo(getStateFactory().createIgnoreInput());
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
