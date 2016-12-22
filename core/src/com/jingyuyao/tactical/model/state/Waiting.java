package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

class Waiting extends AbstractState {
    Waiting(Map map, Markings markings) {
        super(map, markings);
    }

    Waiting(AbstractState prevState) {
        super(prevState);
        getMarkings().unMarkPlayer();
    }

    @Override
    public AbstractState select(Player player) {
        return new Moving(this, player);
    }

    @Override
    public AbstractState select(Enemy enemy) {
        if (getMarkings().getMarkedEnemies().contains(enemy)) {
            getMarkings().unMarkEnemy(enemy);
        } else {
            getMarkings().markEnemy(enemy);
        }
        return this;
    }

    @Override
    public AbstractState select(Terrain terrain) {
        return this;
    }
}
