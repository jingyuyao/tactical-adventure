package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.action.Back;
import com.jingyuyao.tactical.model.action.UseConsumable;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;

// TODO: Fire off an event so the UI show a widget that manages items
public class ChoosingItem extends AbstractPlayerState {
    public ChoosingItem(AbstractState prevState, Player player) {
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
