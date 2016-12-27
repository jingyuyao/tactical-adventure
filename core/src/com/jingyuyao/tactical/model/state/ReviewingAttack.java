package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class ReviewingAttack extends AbstractState {
    private final AttackPlan attackPlan;

    ReviewingAttack(AbstractState prevState, AttackPlan attackPlan) {
        super(prevState);
        this.attackPlan = attackPlan;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        // TODO: show equipped weapon targets only
        getStateMarkings().showImmediateTargets(attackPlan.getAttackingPlayer());
        showAttackPlan(attackPlan);
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getStateMarkings().clearPlayerMarking();
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
