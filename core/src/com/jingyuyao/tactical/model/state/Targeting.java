package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Targeting extends State {
    private final Player targetingPlayer;

    Targeting(State prevState, Player targetingPlayer) {
        super(prevState);
        this.targetingPlayer = targetingPlayer;
        getMarkings().markPlayer(targetingPlayer, true);
        getStateActions().add(this.new WaitAction());
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

    private class WaitAction implements StateAction {
        @Override
        public String getName() {
            return "Wait";
        }

        @Override
        public void run() {
            transitionTo(new Waiting(Targeting.this));
        }
    }
}
