package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class ReviewingAttack extends AbstractState {
    private final AttackPlan attackPlan;

    ReviewingAttack(AbstractState prevState, AttackPlan attackPlan) {
        super(prevState);
        this.attackPlan = attackPlan;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        getMarkings().markEnemyTarget(attackPlan.getAttackPlayer(), attackPlan.getTargetEnemy());
        showAttackPlan(attackPlan);
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
        hideAttackPlan();
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
    ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(
                new Attack(this, attackPlan),
                new Back(this)
        );
    }
}
