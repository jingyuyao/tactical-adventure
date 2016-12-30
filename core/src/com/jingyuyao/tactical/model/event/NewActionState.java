package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.object.Player;

public class NewActionState implements ModelEvent {
    private final Player player;
    private final boolean actionable;

    public NewActionState(Player player, boolean actionable) {
        this.player = player;
        this.actionable = actionable;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isActionable() {
        return actionable;
    }
}
