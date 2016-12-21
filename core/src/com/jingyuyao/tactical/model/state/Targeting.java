package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

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
        Terrain source = getMap().terrains().get(targetingPlayer);
        Collection<Terrain> targetTerrains = getMap().getTargetTerrains(targetingPlayer, source);
        Terrain target = getMap().terrains().get(enemy);
        if (targetTerrains.contains(target)) {
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
