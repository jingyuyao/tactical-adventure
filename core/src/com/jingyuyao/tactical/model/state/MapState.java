package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.state.StateModule.BackingStateStack;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages selection logic.
 */
@Singleton
public class MapState implements SelectionHandler {

  private final EventBus eventBus;
  private final Deque<State> stateStack;

  @Inject
  public MapState(@ModelEventBus EventBus eventBus, @BackingStateStack Deque<State> stateStack) {
    this.eventBus = eventBus;
    this.stateStack = stateStack;
  }

  @Override
  public void select(Player player) {
    eventBus.post(new SelectPlayer(player));
    stateStack.peek().select(player);
  }

  @Override
  public void select(Enemy enemy) {
    eventBus.post(new SelectEnemy(enemy));
    stateStack.peek().select(enemy);
  }

  @Override
  public void select(Terrain terrain) {
    eventBus.post(new SelectTerrain(terrain));
    stateStack.peek().select(terrain);
  }

  public void initialize(State initialState) {
    stateStack.push(initialState);
    initialState.enter();
  }

  public void prepForSave() {
    rollback();
  }

  public void reset() {
    stateStack.peek().exit();
    stateStack.clear();
  }

  public void select(Coordinate coordinate, Cell cell) {
    // TODO: stateStack.peek().select(coordinate, cell);
  }

  /**
   * Go to {@code newState} and add it to the current stack of {@link State}.
   */
  void goTo(State newState) {
    stateStack.peek().exit();
    stateStack.push(newState);
    newState.enter();
  }

  /**
   * Go back to the previous {@link State} if there is one.
   */
  void back() {
    if (stateStack.size() > 1) {
      State currentState = stateStack.pop();
      currentState.exit();
      State lastState = stateStack.peek();
      lastState.canceled();
      lastState.enter();
    }
  }

  /**
   * Go back all the way to the first {@link State} in the current stack.
   */
  void rollback() {
    while (stateStack.size() > 1) {
      State currentState = stateStack.pop();
      currentState.exit();
      State lastState = stateStack.peek();
      lastState.canceled();
      lastState.enter();
    }
  }

  /**
   * Go to {@code startingState} on a new stack.
   */
  void branchTo(State startingState) {
    stateStack.peek().exit();
    stateStack.clear();
    stateStack.push(startingState);
    startingState.enter();
  }

  /**
   * Remove the last state on the stack. Use with caution.
   */
  void popLast() {
    stateStack.pop();
  }
}
