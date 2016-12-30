package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.PlayerContainer;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.ModelEvent;

import javax.inject.Singleton;

@Singleton
public class Turn extends DisposableObject {
    private final PlayerContainer players;
    private int turnCount;

    @Inject
    public Turn(EventBus eventBus, PlayerContainer players) {
        super(eventBus);
        this.players = players;
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
        for (Player player : players) {
            player.setActionable(true);
        }
    }

    public int getTurnCount() {
        return turnCount;
    }

    public static class NewTurn implements ModelEvent {}
}
