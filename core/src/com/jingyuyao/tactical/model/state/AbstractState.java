package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.map.Terrain;

abstract class AbstractState extends EventBusObject implements State {

  private final MapState mapState;
  private final StateFactory stateFactory;

  AbstractState(EventBus eventBus, MapState mapState, StateFactory stateFactory) {
    super(eventBus);
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
    mapState.push(newState);
  }

  void back() {
    mapState.pop();
  }

  void rollback() {
    mapState.rollback();
  }

  void finish(Player player) {
    player.setActionable(false);
    mapState.newStack(stateFactory.createWaiting());
  }

  class Back implements Action {

    @Override
    public String getName() {
      return "back";
    }

    @Override
    public void run() {
      back();
    }
  }
}
