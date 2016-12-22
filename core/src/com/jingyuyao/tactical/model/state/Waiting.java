package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

class Waiting extends State {
    Waiting(Map map, MapState mapState, Markings markings) {
        super(map, mapState, markings);
    }

    Waiting(State prevState) {
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
}
