package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;

class Waiting extends AbstractState {
    /**
     * Used to create the initial state.
     */
    Waiting(MapState mapState, Map map, Turn turn, Markings markings) {
        super(mapState, map, turn, markings);
    }

    Waiting(AbstractState prevState) {
        super(prevState);
    }

    @Override
    void enter() {}

    @Override
    void canceled() {}

    @Override
    void exit() {}

    @Override
    public void select(Player player) {
        if (getTurn().canAct(player)) {
            goTo(new Moving(this, player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        if (getMarkings().getMarkedEnemies().contains(enemy)) {
            getMarkings().unMarkEnemy(enemy);
        } else {
            getMarkings().markEnemy(enemy);
        }
    }

    @Override
    public void select(Terrain terrain) {}

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this));
    }
}
