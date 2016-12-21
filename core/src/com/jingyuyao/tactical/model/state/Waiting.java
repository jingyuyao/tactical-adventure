package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

class Waiting extends AbstractState {
    Waiting(Map map, StateData stateData) {
        super(map, stateData);
    }

    Waiting(AbstractState prevState) {
        super(prevState);
        getStateData().selectedPlayer(null);
    }

    @Override
    public State select(Player player) {
        return new Moving(this, player);
    }

    @Override
    public State select(Enemy enemy) {
        getStateData().selectedEnemy(enemy);
        return this;
    }

    @Override
    public State select(Terrain terrain) {
        return this;
    }
}
