package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * An enemy character
 */
public class Enemy extends Character {
    Enemy(EventBus eventBus, int x, int y, String name, Stats stats, Items items) {
        super(eventBus, x, y, name, stats, items);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }
}
