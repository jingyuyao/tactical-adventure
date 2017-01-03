package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.event.StateChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
@Singleton
public class MapState extends EventBusObject implements ManagedBy<NewMap, ClearMap> {
    private final Deque<State> stateStack;

    @Inject
    public MapState(EventBus eventBus, @BackingStateStack Deque<State> stateStack) {
        super(eventBus);
        this.stateStack = stateStack;
        register();
    }

    @Subscribe
    @Override
    public void initialize(NewMap data) {
        State initialState = data.getInitialState();
        stateStack.push(initialState);
        initialState.enter();
        post(new StateChanged(initialState));
    }

    @Subscribe
    @Override
    public void dispose(ClearMap clearMap) {
        stateStack.clear();
    }

    void push(State newState) {
        stateStack.peek().exit();
        stateStack.push(newState);
        newState.enter();
        post(new StateChanged(newState));
    }

    void pop() {
        if (stateStack.size() > 1) {
            State currentState = stateStack.pop();
            currentState.exit();
            State lastState = stateStack.peek();
            lastState.canceled();
            lastState.enter();
            post(new StateChanged(lastState));
        }
    }

    void rollback() {
        while (stateStack.size() > 1) {
            pop();
        }
    }

    void newStack(State startingState) {
        stateStack.peek().exit();
        stateStack.clear();
        stateStack.push(startingState);
        startingState.enter();
        post(new StateChanged(startingState));
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

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface BackingStateStack {}
}
