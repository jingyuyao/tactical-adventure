package com.jingyuyao.tactical.model;

import java.util.Collection;
import java.util.HashSet;

public class Turn {
    private final Map map;
    private final Collection<Player> actionablePlayers;
    private int turnCount = 0;

    public Turn(Map map) {
        this.map = map;
        actionablePlayers = new HashSet<Player>();
        nextTurn();
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void nextTurn() {
        // TODO: this should somehow alert MapView/UI
        turnCount++;
        actionablePlayers.clear();
        actionablePlayers.addAll(map.getPlayers());
    }

    public boolean canAct(Player player) {
        return actionablePlayers.contains(player);
    }

    public void acted(Player player) {
        actionablePlayers.remove(player);
    }
}
