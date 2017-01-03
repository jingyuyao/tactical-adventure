package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.common.ModelEvent;

public class WaitChange implements ModelEvent {
    private final boolean waiting;

    public WaitChange(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isWaiting() {
        return waiting;
    }
}
