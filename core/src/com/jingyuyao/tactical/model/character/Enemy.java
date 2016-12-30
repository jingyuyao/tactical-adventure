package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;

/**
 * An enemy character
 */
public class Enemy extends Character {
    Enemy(EventBus eventBus, Coordinate coordinate, List<Marker> markers, String name, Stats stats, Items items) {
        super(eventBus, coordinate, markers, name, stats, items);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }
}
