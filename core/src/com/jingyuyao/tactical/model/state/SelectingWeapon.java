package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.TargetInfo;

import javax.inject.Inject;
import java.util.Locale;

class SelectingWeapon extends AbstractPlayerState {
    private final Enemy enemy;
    private final AttackPlanFactory attackPlanFactory;

    @Inject
    SelectingWeapon(EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory, @Assisted Player player, @Assisted Enemy enemy, AttackPlanFactory attackPlanFactory) {
        super(eventBus, mapState, markings, stateFactory, player);
        this.enemy = enemy;
        this.attackPlanFactory = attackPlanFactory;
    }

    @Override
    public void enter() {
        super.enter();
        // TODO: use a different marker for each stage
        getMarkings().showImmediateTargets(getPlayer());
    }

    @Override
    public ImmutableList<Action> getActions() {
        Player player = getPlayer();
        TargetInfo playerInfo = player.createTargetInfo();

        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        ImmutableSet<Weapon> availableWeapons = playerInfo.weaponsFor(player.getCoordinate(), enemy.getCoordinate());
        // Build action using player's weapon list to maintain equipped weapon order
        for (Weapon weapon : player.getWeapons()) {
            if (availableWeapons.contains(weapon)) {
                builder.add(this.new SelectWeapon(weapon));
            }
        }
        builder.add(this.new Back());
        return builder.build();
    }

    class SelectWeapon implements Action {
        private final Weapon weapon;

        SelectWeapon(Weapon weapon) {
            this.weapon = weapon;
        }

        @Override
        public String getName() {
            return String.format(Locale.US, "%s (%d)", weapon.getName(), weapon.getUsageLeft());
        }

        @Override
        public void run() {
            getPlayer().equipWeapon(weapon);
            goTo(getStateFactory().createReviewingAttack(getPlayer(), attackPlanFactory.create(getPlayer(), enemy)));
        }
    }
}
