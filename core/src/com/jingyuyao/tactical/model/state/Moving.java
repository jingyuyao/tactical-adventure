package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
import javax.inject.Inject;

public class Moving extends BasePlayerState {

  private final StateFactory stateFactory;
  private final Movements movements;
  private final Movement movement;

  @Inject
  Moving(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Movements movements,
      @Assisted Player player,
      @Assisted Movement movement) {
    super(eventBus, mapState, stateFactory, player);
    this.stateFactory = stateFactory;
    this.movements = movements;
    this.movement = movement;
  }

  @Override
  public void canceled() {
    getPlayer().instantMoveTo(movement.getStartingCoordinate());
  }

  @Override
  public void select(Cell cell) {
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
      if (!player.equals(getPlayer())) {
        rollback();
        if (player.isActionable()) {
          goTo(stateFactory.createMoving(player, movements.distanceFrom(player)));
        }
      }
    } else {
      if (movement.canMoveTo(cell.getCoordinate())) {
        Path path = movement.pathTo(cell.getCoordinate());
        goTo(stateFactory.createTransition());
        Futures.addCallback(getPlayer().moveAlong(path), new FutureCallback<Void>() {
          @Override
          public void onSuccess(Void result) {
            goTo(stateFactory.createMoved(getPlayer()));
          }

          @Override
          public void onFailure(Throwable t) {

          }
        });
      }
    }
  }

  public Movement getMovement() {
    return movement;
  }
}
