package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

public class ReviewingAttack extends AbstractPlayerState {
    private final AttackPlan attackPlan;

    ReviewingAttack(AbstractState prevState, Player currentPlayer, AttackPlan attackPlan) {
        super(prevState, currentPlayer);
        this.attackPlan = attackPlan;
    }

    @Override
    void enter() {
        super.enter();
        // TODO: use a different marker for each stage
        // TODO: show equipped weapon targets only
        getMarkings().showImmediateTargets(getTargetInfo());
        getEventBus().post(new ShowAttackPlan(attackPlan));
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getMarkings().clearPlayerMarking();
        getEventBus().post(new HideAttackPlan());
    }

    @Override
    public void select(Player player) {
        back();
    }

    @Override
    public void select(Enemy enemy) {
        back();
    }

    @Override
    public void select(Terrain terrain) {
        back();
    }

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(
                new Attack(this, attackPlan),
                new Back(this)
        );
    }

    public static class ShowAttackPlan {
        private final AttackPlan attackPlan;

        private ShowAttackPlan(AttackPlan attackPlan) {
            this.attackPlan = attackPlan;
        }

        public AttackPlan getAttackPlan() {
            return attackPlan;
        }
    }

    public static class HideAttackPlan {
        private HideAttackPlan() {}
    }
}
