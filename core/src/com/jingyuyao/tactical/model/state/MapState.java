package com.jingyuyao.tactical.model.state;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.event.HighlightCharacter;
import com.jingyuyao.tactical.model.state.event.HighlightTerrain;
import com.jingyuyao.tactical.model.state.event.StateChanged;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages selection logic.
 */
@Singleton
public class MapState implements EventSubscriber {

  private final Deque<State> stateStack;
  private EventBus eventBus;
  private MapObject currentHighlight;

  @Inject
  public MapState(@ModelEventBus EventBus eventBus, @BackingStateStack Deque<State> stateStack) {
    this.eventBus = eventBus;
    this.stateStack = stateStack;
  }

  @Subscribe
  public void initialize(NewMap data) {
    State initialState = data.getInitialState();
    stateStack.push(initialState);
    initialState.enter();
    eventBus.post(new StateChanged(initialState));
  }

  @Subscribe
  public void dispose(ClearMap clearMap) {
    currentHighlight = null;
    stateStack.clear();
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

  void push(State newState) {
    stateStack.peek().exit();
    stateStack.push(newState);
    eventBus.post(new StateChanged(newState));
    newState.enter();
  }

  void pop() {
    if (stateStack.size() > 1) {
      State currentState = stateStack.pop();
      currentState.exit();
      State lastState = stateStack.peek();
      lastState.canceled();
      eventBus.post(new StateChanged(lastState));
      lastState.enter();
    }
  }

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

  void newStack(State startingState) {
    stateStack.peek().exit();
    stateStack.clear();
    stateStack.push(startingState);
    eventBus.post(new StateChanged(startingState));
    startingState.enter();
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

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingStateStack {

  }
}
