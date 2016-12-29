package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.Player;

public class Turn {
    private final EventBus eventBus;
    private final Map map;
    private int turnCount = 1;

    public Turn(EventBus eventBus, Map map) {
        this.eventBus = eventBus;
        this.map = map;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void nextTurn() {
        // TODO: this should somehow alert MapView/UI
        turnCount++;
        for (Player player : map.getPlayers()) {
            player.setActionable(true);
        }
    }
}
