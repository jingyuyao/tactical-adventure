package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Player;

public class NewActionState extends ObjectEvent<Player> {
    private final boolean actionable;

    public NewActionState(Player player, boolean actionable) {
        super(player);
        this.actionable = actionable;
    }

    public boolean isActionable() {
        return actionable;
    }
}
