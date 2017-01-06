package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class ShowAttackPlan extends AbstractEvent<AttackPlan> {

  public ShowAttackPlan(AttackPlan attackPlan) {
    super(attackPlan);
  }
}
