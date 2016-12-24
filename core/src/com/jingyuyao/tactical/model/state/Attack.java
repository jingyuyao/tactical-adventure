package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.AttackInfo;

public class Attack extends AbstractAction {
    private final AttackInfo attackInfo;

    Attack(AbstractState state, AttackInfo attackInfo) {
        super(state);
        this.attackInfo = attackInfo;
    }

    @Override
    public String getName() {
        return "attack";
    }

    @Override
    public void run() {
        // TODO: kick off battle animation somewhere
        // TODO: actual calculation time
        getState().getMarkings().unMarkEnemyDangerArea(attackInfo.getTargetEnemy());
        getState().getMap().kill(attackInfo.getTargetEnemy());
        getState().wait(attackInfo.getAttackPlayer());
    }
}
