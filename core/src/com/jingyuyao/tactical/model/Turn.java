package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.Player;

public class Turn {
    private final Map map;
    private int turnCount = 1;

    public Turn(Map map) {
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
