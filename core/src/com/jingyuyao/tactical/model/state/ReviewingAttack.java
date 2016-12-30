package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.action.Attack;
import com.jingyuyao.tactical.model.action.Back;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HideAttackPlan;
import com.jingyuyao.tactical.model.event.ShowAttackPlan;

public class ReviewingAttack extends AbstractPlayerState {
    private final AttackPlan attackPlan;

    public ReviewingAttack(AbstractState prevState, Player currentPlayer, AttackPlan attackPlan) {
        super(prevState, currentPlayer);
        this.attackPlan = attackPlan;
    }

    @Override
    public void enter() {
        super.enter();
        // TODO: use a different marker for each stage
        // TODO: show equipped weapon targets only
        getMarkings().showImmediateTargets(getTargetInfo());
        getEventBus().post(new ShowAttackPlan(attackPlan));
    }

    @Override
    public void exit() {
        getMarkings().clearPlayerMarking();
        getEventBus().post(new HideAttackPlan());
    }

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(
                new Attack(this, attackPlan),
                new Back(this)
        );
    }

}
