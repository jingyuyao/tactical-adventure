package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.map.Cell;

class BaseState implements State {

  private final EventBus eventBus;
  private final WorldState worldState;

  BaseState(EventBus eventBus, WorldState worldState) {
    this.eventBus = eventBus;
    this.worldState = worldState;
  }

  @Override
  public void enter() {
    eventBus.post(this);
  }

  @Override
  public void canceled() {
  }

  @Override
  public void exit() {
    eventBus.post(new ExitState(this));
  }

  @Override
  public void select(Cell cell) {
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of();
  }

  void post(Object event) {
    eventBus.post(event);
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

  void popLast() {
    worldState.popLast();
  }
}
