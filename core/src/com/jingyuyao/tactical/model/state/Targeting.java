package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

public class Targeting extends AbstractState {
    Targeting(AbstractState prevState) {
        super(prevState);
        getSelections().showImmediateTargets();
    }

    @Override
    public SelectionState select(Player player) {
        return new Waiting(this);
    }

    @Override
    public SelectionState select(Enemy enemy) {
        getSelections().removeEnemy(enemy);
        getMap().kill(enemy);
        return new Waiting(this);
    }

    @Override
    public SelectionState select(Terrain terrain) {
        return new Waiting(this);
    }
}
