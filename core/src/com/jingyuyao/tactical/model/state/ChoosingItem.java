package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.object.Player;

// TODO: Fire off an event so the UI show a widget that manages items
class ChoosingItem extends AbstractPlayerState {
    ChoosingItem(AbstractState prevState, Player player) {
        super(prevState, player);
    }

    @Override
    public ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        for (Consumable consumable : getCurrentPlayer().getConsumables()) {
            builder.add(new UseConsumable(this, consumable, getCurrentPlayer()));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
