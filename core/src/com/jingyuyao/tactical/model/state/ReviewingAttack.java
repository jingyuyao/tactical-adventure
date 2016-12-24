package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;

class ReviewingAttack extends AbstractState {
    private final AttackInfo attackInfo;

    ReviewingAttack(AbstractState prevState, AttackInfo attackInfo) {
        super(prevState);
        this.attackInfo = attackInfo;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        getMarkings().markEnemyTarget(attackInfo.getAttackPlayer(), attackInfo.getTargetEnemy());
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
                new Attack(this, attackInfo),
                new Back(this)
        );
    }
}
