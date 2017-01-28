package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HighlightCharacter;
import com.jingyuyao.tactical.model.event.HighlightTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.state.StateModule.BackingStateStack;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages selection logic.
 */
@Singleton
public class MapState {

  private final Deque<State> stateStack;
  private EventBus eventBus;
  private MapObject currentHighlight;

  @Inject
  public MapState(@ModelEventBus EventBus eventBus, @BackingStateStack Deque<State> stateStack) {
    this.eventBus = eventBus;
    this.stateStack = stateStack;
  }

  public void initialize(State initialState) {
    stateStack.push(initialState);
    initialState.enter();
    eventBus.post(new StateChanged(initialState));
  }

  public void select(Player player) {
    stateStack.peek().select(player);
  }

  public void select(Enemy enemy) {
    stateStack.peek().select(enemy);
  }

  public void select(Terrain terrain) {
    stateStack.peek().select(terrain);
  }

  public void highlight(Character character) {
    switchHighlightTo(character);
    eventBus.post(new HighlightCharacter(character));
  }

  public void highlight(Terrain terrain) {
    switchHighlightTo(terrain);
    eventBus.post(new HighlightTerrain(terrain));
  }

  /**
   * Go to {@code newState} and add it to the current stack of {@link State}.
   */
  void goTo(State newState) {
    stateStack.peek().exit();
    stateStack.push(newState);
    eventBus.post(new StateChanged(newState));
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
      eventBus.post(new StateChanged(lastState));
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
      eventBus.post(new StateChanged(lastState));
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
    eventBus.post(new StateChanged(startingState));
    startingState.enter();
  }

  /**
   * Remove the last state on the stack. Use with caution.
   */
  void pop() {
    stateStack.pop();
  }

  private void switchHighlightTo(MapObject highlight) {
    if (currentHighlight != null) {
      currentHighlight.removeMarker(Marker.HIGHLIGHT);
    }
    currentHighlight = highlight;
    if (currentHighlight != null) {
      currentHighlight.addMarker(Marker.HIGHLIGHT);
    }
  }
}
