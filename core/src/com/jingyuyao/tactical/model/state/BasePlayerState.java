package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;

class BasePlayerState extends BaseState implements PlayerState {

  private final StateFactory stateFactory;
  private final Cell playerCell;
  private final Ship player;

  BasePlayerState(
      ModelBus modelBus, WorldState worldState, StateFactory stateFactory, Cell playerCell) {
    super(modelBus, worldState);
    Preconditions.checkArgument(playerCell.player().isPresent());
    this.stateFactory = stateFactory;
    this.playerCell = playerCell;
    this.player = playerCell.player().get();
  }

  @Override
  public Cell getPlayerCell() {
    return playerCell;
  }

  @Override
  public Ship getPlayer() {
    return player;
  }

  StateFactory getStateFactory() {
    return stateFactory;
  }

  void finish() {
    player.setControllable(false);
    post(new Save());
    branchTo(stateFactory.createWaiting());
  }
}
