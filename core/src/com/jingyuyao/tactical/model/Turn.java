package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.ModelEvent;

public class Turn extends DisposableObject {
    private final Map map;
    private int turnCount;

    @Inject
    public Turn(EventBus eventBus, Map map) {
        super(eventBus);
        this.map = map;
        turnCount = 1;
    }

    @Override
    protected void disposed() {
        turnCount = 1;
        super.disposed();
    }

    @Subscribe
    public void newTurn(NewTurn newTurn) {
        turnCount++;
        for (Player player : map.getPlayers()) {
            player.setActionable(true);
        }
    }

    public int getTurnCount() {
        return turnCount;
    }

    public static class NewTurn implements ModelEvent {}
}
