package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.AttackPlan;

public class Attack extends AbstractAction {
    private final AttackPlan attackPlan;

    Attack(AbstractState state, AttackPlan attackPlan) {
        super(state);
        this.attackPlan = attackPlan;
    }

    @Override
    public String getName() {
        return "attack";
    }

    @Override
    public void run() {
        // TODO: kick off battle animation somewhere
        // TODO: actual calculation time
        getState().getMarkings().unMarkEnemyDangerArea(attackPlan.getTargetEnemy());
        getState().getMap().kill(attackPlan.getTargetEnemy());
        getState().wait(attackPlan.getAttackPlayer());
    }
}
