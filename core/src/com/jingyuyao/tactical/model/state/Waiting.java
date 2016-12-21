package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

public class Waiting extends AbstractState {
    public Waiting(Map map, Selections selections) {
        super(map, selections);
    }

    Waiting(AbstractState prevState) {
        super(prevState);
        getSelections().selectedPlayer(null);
    }

    @Override
    public SelectionState select(Player player) {
        return new Moving(this, player);
    }

    @Override
    public SelectionState select(Enemy enemy) {
        getSelections().selectedEnemy(enemy);
        return this;
    }

    @Override
    public SelectionState select(Terrain terrain) {
        return this;
    }
}
