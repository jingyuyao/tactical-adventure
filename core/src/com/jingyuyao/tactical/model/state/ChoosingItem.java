package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

// TODO: Fire off an event so the UI show a widget that manages items
class ChoosingItem extends AbstractPlayerState {
    ChoosingItem(AbstractState prevState, Player player) {
        super(prevState, player);
    }

    @Override
    void canceled() {

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
        for (Consumable consumable : getCurrentPlayer().getConsumables()) {
            builder.add(new UseConsumable(this, consumable, getCurrentPlayer()));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
