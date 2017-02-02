package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

class BaseState implements State {

  private final MapState mapState;
  private final StateFactory stateFactory;

  BaseState(MapState mapState, StateFactory stateFactory) {
    this.mapState = mapState;
    this.stateFactory = stateFactory;
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public void enter() {
  }

  @Override
  public void canceled() {
  }

  @Override
  public void exit() {
  }

  @Override
  public void select(Player player) {
  }

  @Override
  public void select(Enemy enemy) {
  }

  @Override
  public void select(Terrain terrain) {
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of();
  }

  StateFactory getStateFactory() {
    return stateFactory;
  }

  void goTo(State newState) {
    mapState.goTo(newState);
  }

  void back() {
    mapState.back();
  }

  void rollback() {
    mapState.rollback();
  }

  void branchToWait() {
    mapState.branchTo(stateFactory.createWaiting());
  }

  void popLast() {
    mapState.popLast();
  }
}
