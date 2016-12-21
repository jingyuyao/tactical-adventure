package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Targeting extends AbstractState {
    private final Player targetingPlayer;

    Targeting(AbstractState prevState, Player targetingPlayer) {
        super(prevState);
        this.targetingPlayer = targetingPlayer;
        getMarkings().markPlayer(targetingPlayer, true);
    }

    @Override
    public State select(Player player) {
        return new Waiting(this);
    }

    @Override
    public State select(Enemy enemy) {
        if (getMap().canImmediateTarget(targetingPlayer, enemy)) {
            // TODO: enter battle prep
            getMarkings().removeEnemy(enemy);
            getMap().kill(enemy);
        }
        return new Waiting(this);
    }

    @Override
    public State select(Terrain terrain) {
        return new Waiting(this);
    }
}
