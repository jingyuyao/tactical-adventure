package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Targeting extends AbstractState {
    Targeting(AbstractState prevState) {
        super(prevState);
        getStateData().showImmediateTargets();
    }

    @Override
    public State select(Player player) {
        return new Waiting(this);
    }

    @Override
    public State select(Enemy enemy) {
        getStateData().removeEnemy(enemy);
        getMap().kill(enemy);
        return new Waiting(this);
    }

    @Override
    public State select(Terrain terrain) {
        return new Waiting(this);
    }
}
