package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.state.AbstractState;

public class Attack extends AbstractAction {
    private final AttackPlan attackPlan;

    public Attack(AbstractState state, AttackPlan attackPlan) {
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
        attackPlan.execute();
        getState().finish(attackPlan.getAttackingPlayer());
    }
}
