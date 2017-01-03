package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.mark.Markings;

import javax.inject.Inject;

class Choosing extends AbstractPlayerState {
    @Inject
    Choosing(EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory, @Assisted Player player) {
        super(eventBus, mapState, markings, stateFactory, player);
    }

    @Override
    public void enter() {
        super.enter();
        getMarkings().showImmediateTargets(getPlayer());
    }

    @Override
    public void select(Player player) {
        if (Objects.equal(getPlayer(), player)) {
            back();
        } else {
            rollback();
            goTo(getStateFactory().createMoving(player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        if (getPlayer().createTargetInfo().canHitImmediately(enemy)) {
            goTo(getStateFactory().createSelectingWeapon(getPlayer(), enemy));
        } else {
            back();
        }
    }

    @Override
    public ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        if (!Iterables.isEmpty(getPlayer().getConsumables())) {
            builder.add(this.new UseItems());
        }
        builder.add(this.new Wait());
        builder.add(this.new Back());
        return builder.build();
    }

    class UseItems implements Action {
        @Override
        public String getName() {
            return "items";
        }

        @Override
        public void run() {
            goTo(getStateFactory().createChoosingItem(getPlayer()));
        }
    }
}
