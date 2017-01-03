package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.event.ObjectEvent;

public class ShowAttackPlan extends ObjectEvent<AttackPlan> {
    public ShowAttackPlan(AttackPlan attackPlan) {
        super(attackPlan);
    }
}
