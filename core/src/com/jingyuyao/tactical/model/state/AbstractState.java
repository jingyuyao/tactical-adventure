package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

abstract class AbstractState implements State {

  private final MapState mapState;
  private final StateFactory stateFactory;

  AbstractState(MapState mapState, StateFactory stateFactory) {
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
    back();
  }

  @Override
  public void select(Enemy enemy) {
    back();
  }

  @Override
  public void select(Terrain terrain) {
    back();
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

  void pop() {
    mapState.pop();
  }
}
