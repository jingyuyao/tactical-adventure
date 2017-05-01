package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
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
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      @Assisted Cell cell,
      @Assisted Movement movement) {
    super(modelBus, worldState, stateFactory, cell);
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
    if (cell.player().isPresent()) {
      Player player = cell.player().get();
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
        movement.getStartingCell().moveCharacter(path).addCallback(new Runnable() {
          @Override
          public void run() {
            goTo(getStateFactory().createMoved(cell));
          }
        });
      }
    }
  }

  public Movement getMovement() {
    return movement;
  }
}
