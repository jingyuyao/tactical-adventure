package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;

class Targeting extends AbstractState {
    private final Player targetingPlayer;

    Targeting(AbstractState prevState, Player targetingPlayer) {
        super(prevState);
        this.targetingPlayer = targetingPlayer;
        getMarkings().markPlayer(targetingPlayer, true);
        getActions().add(this.new TestAction());
    }

    @Override
    public AbstractState select(Player player) {
        return new Waiting(this);
    }

    @Override
    public AbstractState select(Enemy enemy) {
        if (getMap().canImmediateTarget(targetingPlayer, enemy)) {
            // TODO: enter battle prep
            getMarkings().removeEnemy(enemy);
            getMap().kill(enemy);
        }
        return new Waiting(this);
    }

    @Override
    public AbstractState select(Terrain terrain) {
        return new Waiting(this);
    }

    private class TestAction implements Action {
        @Override
        public String getName() {
            return "Test123";
        }

        @Override
        public Runnable getRunnable() {
            return new Runnable() {
                @Override
                public void run() {
                    transitionTo(waiting());
                }
            };
        }
    }
}
