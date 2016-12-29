package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.TargetInfoFactory;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.action.EndTurn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

import javax.inject.Inject;

public class Waiting extends AbstractState {
    /**
     * Waiting states do NOT have a {@link AbstractState#prevState}.
     */
    @Inject
    public Waiting(EventBus eventBus, Markings markings, TargetInfoFactory targetInfoFactory, AttackPlanFactory attackPlanFactory) {
        super(eventBus, null, markings, targetInfoFactory, attackPlanFactory);
    }

    @Override
    public void select(Player player) {
        if (player.isActionable()) {
            goTo(new Moving(this, player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        getMarkings().toggleDangerArea(getTargetInfoFactory().create(enemy));
    }

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(new EndTurn(this, getEventBus()));
    }
}
