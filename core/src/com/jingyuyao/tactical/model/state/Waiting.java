package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.Turn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class Waiting extends AbstractState {
    /**
     * Used to create the initial state.
     */
    Waiting(MapState mapState, Map map, Turn turn, MarkingFactory markingFactory) {
        super(map, mapState, turn, markingFactory);
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
        // TODO: add something like a persistent markings list that gets passed between states
        // markings should only belong in the states and not on individual objects,
        enemy.toggleDangerMarking(getMarkingFactory().danger(enemy));
    }

    @Override
    public void select(Terrain terrain) {}

    @Override
    ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this));
    }
}
