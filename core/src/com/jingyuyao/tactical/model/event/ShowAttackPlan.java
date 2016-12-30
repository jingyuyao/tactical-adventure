package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.AttackPlan;

public class ShowAttackPlan implements ModelEvent {
    private final AttackPlan attackPlan;

    public ShowAttackPlan(AttackPlan attackPlan) {
        this.attackPlan = attackPlan;
    }

    public AttackPlan getAttackPlan() {
        return attackPlan;
    }
}
