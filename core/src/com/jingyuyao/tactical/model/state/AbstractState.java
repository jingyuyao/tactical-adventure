package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.Turn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

abstract class AbstractState implements State {
    private final EventBus eventBus;
    private final AbstractState prevState;
    private final Turn turn;
    private final Markings markings;
    private final TargetInfo.Factory targetInfoFactory;
    private final AttackPlan.Factory attackPlanFactory;

    /**
     * Creates a new state with all of previous state's data and set {@link #prevState} of the new state
     * to the old state.
     */
    AbstractState(AbstractState prevState) {
        this(prevState.eventBus, prevState, prevState.turn, prevState.markings, prevState.targetInfoFactory, prevState.attackPlanFactory);
    }

    /**
     * Creates a new state with the given data and set {@link #prevState} to null.
     */
    AbstractState(EventBus eventBus, Turn turn, Markings markings, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        this(eventBus, null, turn, markings, targetInfoFactory, attackPlanFactory);
    }

    /**
     * Creates a new state with the given data.
     */
    private AbstractState(EventBus eventBus, AbstractState prevState, Turn turn, Markings markings, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        this.eventBus = eventBus;
        this.prevState = prevState;
        this.turn = turn;
        this.markings = markings;
        this.targetInfoFactory = targetInfoFactory;
        this.attackPlanFactory = attackPlanFactory;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    EventBus getEventBus() {
        return eventBus;
    }

    Markings getMarkings() {
        return markings;
    }

    TargetInfo.Factory getTargetInfoFactory() {
        return targetInfoFactory;
    }

    AttackPlan.Factory getAttackPlanFactory() {
        return attackPlanFactory;
    }

    void nextTurn() {
        turn.nextTurn();
    }

    void goTo(AbstractState newState) {
        eventBus.post(new StateChange(newState));
    }

    /**
     * Cancel {@link #prevState} then {@link #goTo(AbstractState)} it
     */
    void back() {
        if (prevState != null) {
            prevState.canceled();
            goTo(prevState);
        }
    }

    /**
     * Go {@link #back()} state by state until we reach a {@link AbstractState} without {@link #prevState}.
     * @return The {@link AbstractState} we went back to, useful for "reset then go to a new state" scenario
     */
    AbstractState backToOrigin() {
        // Will recursively loop until we encounter a state without prevState
        if (prevState != null) {
            back();
            return prevState.backToOrigin();
        } else {
            return this;
        }
    }

    /**
     * Finished acting on {@code player} and then go to a new waiting state.
     */
    void wait(Player player) {
        player.setActionable(false);
        goTo(new Waiting(eventBus, turn, markings, targetInfoFactory, attackPlanFactory));
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
}
