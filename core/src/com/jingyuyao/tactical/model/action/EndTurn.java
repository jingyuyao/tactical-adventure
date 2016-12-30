package com.jingyuyao.tactical.model.action;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.event.NewTurn;
import com.jingyuyao.tactical.model.state.AbstractState;

public class EndTurn extends AbstractAction {
    private final EventBus eventBus;

    public EndTurn(AbstractState state, EventBus eventBus) {
        super(state);
        this.eventBus = eventBus;
    }

    @Override
    public String getName() {
        return "End Turn";
    }

    @Override
    public void run() {
        eventBus.post(new NewTurn());
    }
}
