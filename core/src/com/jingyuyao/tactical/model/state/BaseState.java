package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.terrain.Terrain;

class BaseState implements State {

  private final EventBus eventBus;
  private final MapState mapState;

  BaseState(EventBus eventBus, MapState mapState) {
    this.eventBus = eventBus;
    this.mapState = mapState;
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

  void post(Object event) {
    eventBus.post(event);
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

  void branchTo(State state) {
    mapState.branchTo(state);
  }

  void popLast() {
    mapState.popLast();
  }
}
