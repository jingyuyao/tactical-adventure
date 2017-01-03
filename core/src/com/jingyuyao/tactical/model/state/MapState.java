package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.common.EventObject;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.model.map.Terrain;

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
public class MapState extends EventObject implements Disposable {
    private final Waiter waiter;
    private final Deque<State> stateStack;

    @Inject
    public MapState(EventBus eventBus, Waiter waiter, @BackingStateStack Deque<State> stateStack) {
        super(eventBus);
        this.waiter = waiter;
        this.stateStack = stateStack;
    }

    @Override
    public void dispose() {
        stateStack.clear();
    }

    public void initialize(State startingState) {
        stateStack.push(startingState);
        startingState.enter();
        post(new StateChanged(startingState));
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
        if (waiter.isWaiting()) return;
        stateStack.peek().select(player);
    }

    public void select(Enemy enemy) {
        if (waiter.isWaiting()) return;
        stateStack.peek().select(enemy);
    }

    public void select(Terrain terrain) {
        if (waiter.isWaiting()) return;
        stateStack.peek().select(terrain);
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface BackingStateStack {}
}
