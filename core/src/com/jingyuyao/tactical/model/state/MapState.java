package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.state.StateModule.BackingStateStack;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages selection logic.
 */
@Singleton
public class MapState {

  private final Deque<State> stateStack;

  @Inject
  public MapState(@BackingStateStack Deque<State> stateStack) {
    this.stateStack = stateStack;
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

  public void select(Cell cell) {
    stateStack.peek().select(cell);
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
