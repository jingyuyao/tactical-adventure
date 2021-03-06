package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Collections;
import java.util.List;

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
  public List<Action> getActions() {
    return Collections.emptyList();
  }

  @Override
  public Turn getTurn() {
    return worldState.getTurn();
  }

  ModelBus getModelBus() {
    return modelBus;
  }

  void post(Object event) {
    modelBus.post(event);
  }

  void advanceTurn() {
    worldState.advanceTurn();
  }

  Script getScript() {
    return worldState.getScript();
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
}
