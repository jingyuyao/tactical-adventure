package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.BattleInfo;

public class Attack extends AbstractAction {
    private final BattleInfo battleInfo;

    Attack(AbstractState state, BattleInfo battleInfo) {
        super(state);
        this.battleInfo = battleInfo;
    }

    @Override
    public String getName() {
        return "attack";
    }

    @Override
    public void run() {
        // TODO: kick off battle animation somewhere
        // TODO: actual calculation time
        getState().getMarkings().unMarkEnemyDangerArea(battleInfo.getTargetEnemy());
        getState().getMap().kill(battleInfo.getTargetEnemy());
        getState().wait(battleInfo.getAttackPlayer());
    }
}
