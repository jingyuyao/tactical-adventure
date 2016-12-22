package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Targeting extends AbstractState {
    private final Player currentPlayer;

    Targeting(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        getMarkings().markPlayer(currentPlayer, true);
    }

    @Override
    void canceled() {}

    @Override
    void exit() {
        getMarkings().unMarkPlayer();
    }

    @Override
    public void select(Player player) {
        hardCancel();
    }

    @Override
    public void select(Enemy enemy) {
        if (getMap().canImmediateTarget(currentPlayer, enemy)) {
            // TODO: enter battle prep
            getMarkings().removeEnemy(enemy);
            getMap().kill(enemy);
            finish(currentPlayer);
        } else {
            hardCancel();
        }
    }

    @Override
    public void select(Terrain terrain) {
        hardCancel();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new Back(this), new Finish(this, currentPlayer));
    }
}
