package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

import java.util.ArrayList;
import java.util.Collection;

class Waiting extends AbstractState {
    Waiting(Map map, Markings markings) {
        super(map, markings);
    }

    Waiting(AbstractState prevState) {
        super(prevState);
        getMarkings().unMarkPlayer();
    }

    @Override
    public State select(Player player) {
        return new Moving(this, player);
    }

    @Override
    public State select(Enemy enemy) {
        if (getMarkings().getMarkedEnemies().contains(enemy)) {
            getMarkings().unMarkEnemy(enemy);
        } else {
            getMarkings().markEnemy(enemy);
        }
        return this;
    }

    @Override
    public State select(Terrain terrain) {
        return this;
    }

    @Override
    public Collection<Action> getActions() {
        return new ArrayList<Action>();
    }
}
