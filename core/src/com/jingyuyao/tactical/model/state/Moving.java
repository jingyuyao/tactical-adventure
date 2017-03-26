package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
import javax.inject.Inject;

public class Moving extends PlayerActionState {

  private final Movements movements;
  private final Movement movement;
  private Cell prevMove;

  @Inject
  Moving(
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      @Assisted Cell cell,
      @Assisted Movement movement) {
    super(eventBus, worldState, stateFactory, cell);
    this.movements = movements;
    this.movement = movement;
  }

  @Override
  public void canceled() {
    if (prevMove != null) {
      prevMove.instantMoveCharacter(movement.getStartingCell());
    }
  }

  @Override
  public void select(final Cell cell) {
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
      if (!player.equals(getPlayer())) {
        rollback();
        if (player.isActionable()) {
          goTo(getStateFactory().createMoving(cell, movements.distanceFrom(cell)));
        }
      }
    } else {
      if (movement.canMoveTo(cell)) {
        Path path = movement.pathTo(cell);
        prevMove = cell;
        goTo(getStateFactory().createTransition());
        Futures.addCallback(movement.getStartingCell().moveCharacter(path),
            new FutureCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                goTo(getStateFactory().createMoved(cell));
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
