package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends AbstractState {
    private final Player player;

    AbstractPlayerState(EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory, Player player) {
        super(eventBus, mapState, markings, stateFactory);
        this.player = player;
    }

    @Override
    public void exit() {
        getMarkings().clearPlayerMarking();
        super.exit();
    }

    Player getPlayer() {
        return player;
    }

    class Wait implements Action {
        @Override
        public String getName() {
            return "wait";
        }

        @Override
        public void run() {
            finish(player);
        }
    }
}
