package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.world.Cell;

class BaseState implements State {

  private final ModelBus modelBus;
  private final WorldState worldState;

  BaseState(ModelBus modelBus, WorldState worldState) {
    this.modelBus = modelBus;
    this.worldState = worldState;
  }

  @Override
  public void enter() {
    modelBus.post(this);
  }

  @Override
  public void canceled() {
  }

  @Override
  public void exit() {
    modelBus.post(new ExitState(this));
  }

  @Override
  public void select(Cell cell) {
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of();
  }

  void post(Object event) {
    modelBus.post(event);
  }

  void goTo(State newState) {
    worldState.goTo(newState);
  }

  void back() {
    worldState.back();
  }

  void rollback() {
    worldState.rollback();
  }

  void branchTo(State state) {
    worldState.branchTo(state);
  }

  void removeSelf() {
    worldState.remove(this);
  }

  Turn getTurn() {
    return worldState.getTurn();
  }

  Optional<ScriptActions> currentTurnScript() {
    return worldState.getScript().turnScript(worldState.getTurn());
  }
}
