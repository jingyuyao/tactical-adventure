package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.event.StateChange;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.DisposableObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
@Singleton
public class MapState extends DisposableObject {
    private final Waiter waiter;
    private final State initialState;
    private State state;

    @Inject
    public MapState(EventBus eventBus, Waiter waiter, @InitialState State state) {
        super(eventBus);
        this.waiter = waiter;
        // TODO: add something like MapState.begin() so we can fire off a state change event to the view
        this.initialState = state;
        this.state = state;
    }

    @Override
    protected void disposed() {
        state = initialState;
        super.disposed();
    }

    public ImmutableList<Action> getActions() {
        return state.getActions();
    }

    public void select(Player player) {
        if (waiter.isWaiting()) return;
        state.select(player);
    }

    public void select(Enemy enemy) {
        if (waiter.isWaiting()) return;
        state.select(enemy);
    }

    public void select(Terrain terrain) {
        if (waiter.isWaiting()) return;
        state.select(terrain);
    }

    @Subscribe
    public void stateChange(StateChange stateChange) {
        state.exit();
        state = stateChange.getNewState();
        state.enter();
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialState {}
}
