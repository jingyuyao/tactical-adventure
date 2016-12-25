package com.jingyuyao.tactical.model;

import java.util.HashSet;
import java.util.Set;

public class Turn {
    private final Map map;
    private final Set<Player> actionablePlayers;
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
        for (Player player : map.getPlayers()) {
            player.setActionable(true);
            actionablePlayers.add(player);
        }
    }

    public boolean canAct(Player player) {
        return actionablePlayers.contains(player);
    }

    public void acted(Player player) {
        actionablePlayers.remove(player);
        player.setActionable(false);
    }
}
