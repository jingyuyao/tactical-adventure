package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.StateModule.BackingStateStack;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldState {

  private final StateFactory stateFactory;
  private final Deque<State> stateStack;
  private final ModelBus modelBus;
  private Turn turn;
  private Script script;

  @Inject
  public WorldState(
      StateFactory stateFactory,
      @BackingStateStack Deque<State> stateStack,
      ModelBus modelBus) {
    this.stateFactory = stateFactory;
    this.stateStack = stateStack;
    this.modelBus = modelBus;
  }

  public void initialize(Turn turn, Script script) {
    Preconditions.checkNotNull(script);
    this.turn = turn;
    this.script = script;
    State initialState;
    switch (turn.getStage()) {
      case START:
        initialState = stateFactory.createStartTurn();
        break;
      case PLAYER:
        initialState = stateFactory.createWaiting();
        break;
      case END:
        initialState = stateFactory.createEndTurn();
        break;
      case ENEMY:
        initialState = stateFactory.createRetaliating();
        break;
      default:
        throw new RuntimeException("missing initial state for a turn stage");
    }
    stateStack.push(initialState);
    stateStack.peek().enter();
  }

  public void reset() {
    stateStack.peek().exit();
    stateStack.clear();
    turn = null;
    script = null;
  }

  public void select(Cell cell) {
    modelBus.post(new SelectCell(cell));
    stateStack.peek().select(cell);
  }

  public Turn getTurn() {
    return turn;
  }

  public Script getScript() {
    return script;
  }

  void advanceTurn() {
    turn = turn.advance();
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
