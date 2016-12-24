package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.BattleInfo;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class ReviewingAttack extends AbstractState {
    private final BattleInfo battleInfo;

    ReviewingAttack(AbstractState prevState, BattleInfo battleInfo) {
        super(prevState);
        this.battleInfo = battleInfo;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        getMarkings().markEnemyTarget(battleInfo.getAttackPlayer(), battleInfo.getTargetEnemy());
        // TODO: how do we sent info to ui?
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
    }

    @Override
    void select(Player player) {
        back();
    }

    @Override
    void select(Enemy enemy) {
        back();
    }

    @Override
    void select(Terrain terrain) {
        back();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(
                new Attack(this, battleInfo),
                new Back(this)
        );
    }
}
