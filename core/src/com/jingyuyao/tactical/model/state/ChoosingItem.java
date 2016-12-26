package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class ChoosingItem extends AbstractState {
    private final Player player;

    ChoosingItem(AbstractState prevState, Player player) {
        super(prevState);
        this.player = player;
    }

    @Override
    void enter() {
        // TODO: Fire off an event so the UI show a widget that manages items
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {

    }

    @Override
    void select(Player player) {
        back();
    }

    @Override
    void select(Enemy enemy) {
        back();
    }

    @Override
    void select(Terrain terrain) {
        back();
    }

    @Override
    ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        for (Consumable consumable : player.getConsumables()) {
            builder.add(new UseConsumable(this, consumable, player));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
