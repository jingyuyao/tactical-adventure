package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.AttackPlan;

public class ShowAttackPlan extends ObjectEvent<AttackPlan> {
    public ShowAttackPlan(AttackPlan attackPlan) {
        super(attackPlan);
    }
}
