package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.state.StateModule.BackingStateStack;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldState {

  private final StateFactory stateFactory;
  private final Deque<State> stateStack;
  private int turn = 1;

  @Inject
  public WorldState(StateFactory stateFactory, @BackingStateStack Deque<State> stateStack) {
    this.stateFactory = stateFactory;
    this.stateStack = stateStack;
  }

  public int getTurn() {
    return turn;
  }

  public void initialize(int turn) {
    this.turn = turn;
    stateStack.push(stateFactory.createStartTurn());
    stateStack.peek().enter();
  }

  public void prepForSave() {
    rollback();
  }

  public void reset() {
    stateStack.peek().exit();
    stateStack.clear();
    turn = 1;
  }

  public void select(Cell cell) {
    stateStack.peek().select(cell);
  }

  void incrementTurn() {
    turn++;
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
   * Remove a state on the stack. Use with caution.
   */
  void remove(State state) {
    stateStack.remove(state);
  }
}
