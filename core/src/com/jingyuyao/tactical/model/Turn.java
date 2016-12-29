package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.util.ModelEvent;

public class Turn {
    private final Map map;
    private int turnCount = 1;

    public Turn(EventBus eventBus, Map map) {
        this.map = map;
        eventBus.register(this);
    }

    public int getTurnCount() {
        return turnCount;
    }

    @Subscribe
    public void newTurn(NewTurn newTurn) {
        turnCount++;
        for (Player player : map.getPlayers()) {
            player.setActionable(true);
        }
    }

    public static class NewTurn implements ModelEvent {}
}
