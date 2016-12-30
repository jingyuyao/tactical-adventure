package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.TargetInfoFactory;
import com.jingyuyao.tactical.model.event.StateChange;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.util.DisposableObject;

public abstract class AbstractState extends DisposableObject implements State {
    private final AbstractState prevState;
    private final Markings markings;
    private final TargetInfoFactory targetInfoFactory;
    private final AttackPlanFactory attackPlanFactory;

    /**
     * Creates a new state with all of previous state's data and set {@link #prevState} of the new state
     * to the old state.
     */
    AbstractState(AbstractState prevState) {
        this(prevState.getEventBus(), prevState, prevState.markings, prevState.targetInfoFactory, prevState.attackPlanFactory);
    }

    /**
     * Creates a new state with the given data.
     */
    AbstractState(EventBus eventBus, AbstractState prevState, Markings markings, TargetInfoFactory targetInfoFactory, AttackPlanFactory attackPlanFactory) {
        super(eventBus);
        this.prevState = prevState;
        this.markings = markings;
        this.targetInfoFactory = targetInfoFactory;
        this.attackPlanFactory = attackPlanFactory;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
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

    public void goTo(AbstractState newState) {
        getEventBus().post(new StateChange(newState));
    }

    /**
     * Cancel {@link #prevState} then {@link #goTo(AbstractState)} it
     */
    public void back() {
        if (prevState != null) {
            prevState.canceled();
            goTo(prevState);
        }
    }

    /**
     * Finished acting on {@code player} and then go to a new waiting state.
     */
    public void finish(Player player) {
        player.setActionable(false);
        goTo(new Waiting(getEventBus(), markings, targetInfoFactory, attackPlanFactory));
    }

    Markings getMarkings() {
        return markings;
    }

    TargetInfoFactory getTargetInfoFactory() {
        return targetInfoFactory;
    }

    AttackPlanFactory getAttackPlanFactory() {
        return attackPlanFactory;
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
}
