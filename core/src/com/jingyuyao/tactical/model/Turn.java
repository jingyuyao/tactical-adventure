package com.jingyuyao.tactical.model;

public class Turn {
    private final Map map;
    private int turnCount = 0;

    public Turn(Map map) {
        this.map = map;
        nextTurn();
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
