package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;

class Waiting extends AbstractState {
    /**
     * Used to create the initial state.
     */
    Waiting(MapState mapState, Map map, Turn turn) {
        super(mapState, map, turn);
    }

    /**
     * Should only be used within {@link AbstractState#wait(Player)}!
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
        if (player.isActionable()) {
            goTo(new Moving(this, player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        enemy.setShowDangerArea(!enemy.isShowDangerArea());
    }

    @Override
    public void select(Terrain terrain) {}

    @Override
    ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this));
    }
}
