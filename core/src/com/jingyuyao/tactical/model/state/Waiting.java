package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;

class Waiting extends AbstractState {
    /**
     * Used to create the initial state.
     */
    Waiting(MapState mapState, Map map, Turn turn, AnimationCounter animationCounter, Markings markings) {
        super(mapState, map, turn, animationCounter, markings);
    }

    /**
     * Should only be used within {@link AbstractState#done(Player)}!
     */
    Waiting(AbstractState prevState) {
        super(prevState);
    }

    @Override
    void enter() {}

    @Override
    void canceled() {}

    @Override
    void exit() {}

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
        if (getTurn().canAct(player)) {
            goTo(new Moving(this, player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        if (getMarkings().getMarkedEnemies().contains(enemy)) {
            getMarkings().unMarkEnemyDangerArea(enemy);
        } else {
            getMarkings().markEnemyDangerArea(enemy);
        }
    }

    @Override
    public void select(Terrain terrain) {}

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this));
    }
}
