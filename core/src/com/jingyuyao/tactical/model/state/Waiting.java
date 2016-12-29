package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.Turn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class Waiting extends AbstractState {
    /**
     * Used to create the initial state.
     */
    Waiting(EventBus eventBus, MapState mapState, Turn turn, MarkingFactory markingFactory, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        super(eventBus, mapState, turn, new Markings(eventBus, markingFactory), targetInfoFactory, attackPlanFactory);
    }

    /**
     * Should only be used within {@link AbstractState#wait(Player)}!
     */
    Waiting(AbstractState prevState) {
        super(prevState);
    }

    /**
     * Do nothing.
     */
    @Override
    void back() {}

    /**
     * Returning itself as per {@link AbstractState#backToWaiting()} spec.
     */
    @Override
    Waiting backToWaiting() {
        return this;
    }

    @Override
    public void select(Player player) {
        if (player.isActionable()) {
            goTo(new Moving(this, player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        getMarkings().toggleDangerArea(getTargetInfoFactory().create(enemy));
    }

    @Override
    public void select(Terrain terrain) {}

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this));
    }
}
